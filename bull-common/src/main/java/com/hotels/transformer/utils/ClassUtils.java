/**
 * Copyright (C) 2019-2021 Expedia, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hotels.transformer.utils;

import static java.lang.invoke.LambdaMetafactory.metafactory;
import static java.lang.invoke.MethodHandles.lookup;
import static java.lang.invoke.MethodHandles.privateLookupIn;
import static java.lang.invoke.MethodType.methodType;
import static java.lang.reflect.Modifier.isFinal;
import static java.lang.reflect.Modifier.isPrivate;
import static java.lang.reflect.Modifier.isPublic;
import static java.lang.reflect.Modifier.isStatic;
import static java.util.Arrays.asList;
import static java.util.Arrays.stream;
import static java.util.Collections.max;
import static java.util.Comparator.comparing;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static java.util.Optional.ofNullable;
import static java.util.Set.of;
import static java.util.stream.Collectors.toList;

import static com.hotels.transformer.base.Defaults.defaultValue;
import static com.hotels.transformer.cache.CacheManagerFactory.getCacheManager;
import static com.hotels.transformer.constant.ClassType.IMMUTABLE;
import static com.hotels.transformer.constant.ClassType.MIXED;
import static com.hotels.transformer.constant.ClassType.MUTABLE;
import static com.hotels.transformer.constant.Filters.IS_FINAL_AND_NOT_STATIC_FIELD;
import static com.hotels.transformer.constant.Filters.IS_NOT_FINAL_AND_NOT_STATIC_FIELD;
import static com.hotels.transformer.constant.Filters.IS_NOT_FINAL_FIELD;
import static com.hotels.transformer.validator.Validator.notNull;

import java.lang.annotation.Annotation;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.Currency;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;
import java.util.function.Predicate;
import java.util.function.Supplier;

import com.hotels.transformer.cache.CacheManager;
import com.hotels.transformer.constant.ClassType;
import com.hotels.transformer.error.InstanceCreationException;
import com.hotels.transformer.error.InvalidBeanException;
import com.hotels.transformer.error.MissingMethodException;

/**
 * Reflection utils for Class objects.
 */
public final class ClassUtils {
    /**
     * Default method name used by a Builder for creating an object.
     */
    public static final String BUILD_METHOD_NAME = "build";

    /**
     * Class nullability error message constant.
     */
    private static final String CLAZZ_CANNOT_BE_NULL = "clazz cannot be null!";

    /**
     * CacheManager class {@link CacheManager}.
     */
    private static final CacheManager CACHE_MANAGER = getCacheManager("classUtils");

    /**
     * Primitive types list.
     */
    private static final Set<Class<?>> PRIMITIVE_TYPES = of(String.class, Boolean.class, Integer.class, Long.class,
           Double.class, BigDecimal.class, BigInteger.class, Short.class, Float.class, Character.class, Byte.class, Void.class);

    /**
     * Method Handles lookup.
     */
    private static final MethodHandles.Lookup METHOD_HANDLES_LOOKUP = lookup();

    /**
     * Reflection utils instance {@link ReflectionUtils}.
     */
    private final ReflectionUtils reflectionUtils;

    /**
     * Default constructor.
     */
    public ClassUtils() {
        this.reflectionUtils = new ReflectionUtils();
    }

    /**
     * Checks if an object is a primitive or special type.
     * @param clazz the class to check
     * @return true if is primitive or special type, false otherwise
     */
    public boolean isPrimitiveOrSpecialType(final Class<?> clazz) {
        if (isNull(clazz)) {
            return false;
        }
        final String cacheKey = "isPrimitiveOrSpecial-" + clazz.getName();
        return CACHE_MANAGER.getFromCache(cacheKey, Boolean.class).orElseGet(() -> {
            final Boolean res = isPrimitiveType(clazz) || isSpecialType(clazz);
            CACHE_MANAGER.cacheObject(cacheKey, res);
            return res;
        });
    }

    /**
     * Checks if an object is a special type.
     * @param clazz the class to check
     * @return true if is special type, false otherwise
     */
    public boolean isPrimitiveType(final Class<?> clazz) {
        final String cacheKey = "isPrimitive-" + clazz.getName();
        return CACHE_MANAGER.getFromCache(cacheKey, Boolean.class).orElseGet(() -> {
            final Boolean res = clazz.isPrimitive() || PRIMITIVE_TYPES.contains(clazz) || clazz.isEnum();
            CACHE_MANAGER.cacheObject(cacheKey, res);
            return res;
        });
    }

    /**
     * Checks if an object is a primitive type array.
     * @param clazz the class to check
     * @return true if is primitive type array, false otherwise
     */
    public boolean isPrimitiveTypeArray(final Class<?> clazz) {
        final String cacheKey = "isPrimitiveTypeArray-" + clazz.getName();
        return CACHE_MANAGER.getFromCache(cacheKey, Boolean.class).orElseGet(() -> {
            final Boolean res = clazz.isArray() && isPrimitiveType(clazz.getComponentType());
            CACHE_MANAGER.cacheObject(cacheKey, res);
            return res;
        });
    }

    /**
     * Checks if an object is a special type.
     * The label "Special classes" refers to all objects that has to be copied without applying any special transformation.
     * @param clazz the class to check
     * @return true if is special type, false otherwise
     */
    public boolean isSpecialType(final Class<?> clazz) {
        final String cacheKey = "isSpecial-" + clazz.getName();
        return CACHE_MANAGER.getFromCache(cacheKey, Boolean.class).orElseGet(() -> {
            final Boolean res = clazz.equals(Currency.class) || clazz.equals(Locale.class) || Temporal.class.isAssignableFrom(clazz)
                    || clazz.equals(Date.class) || clazz.equals(Properties.class) || clazz.isSynthetic();
            CACHE_MANAGER.cacheObject(cacheKey, res);
            return res;
        });
    }

    /**
     * Checks if the given type is a {@link Double}.
     * @param type the class to check
     * @return true if is Double
     */
    public static boolean isDouble(final Class<?> type) {
        return Double.class.isAssignableFrom(type) || type == double.class;
    }

    /**
     * Checks if the given type is a {@link Float}.
     * @param type the class to check
     * @return true if is Float
     */
    public static boolean isFloat(final Class<?> type) {
        return Float.class.isAssignableFrom(type) || type == float.class;
    }

    /**
     * Checks if the given type is a {@link Long}.
     * @param type the class to check
     * @return true if is Long
     */
    public static boolean isLong(final Class<?> type) {
        return Long.class.isAssignableFrom(type) || type == long.class;
    }

    /**
     * Checks if the given type is a {@link Short}.
     * @param type the class to check
     * @return true if is Short
     */
    public static boolean isShort(final Class<?> type) {
        return Short.class.isAssignableFrom(type) || type == short.class;
    }

    /**
     * Checks if the given type is an {@link Integer}.
     * @param type the class to check
     * @return true if is Integer
     */
    public static boolean isInt(final Class<?> type) {
        return Integer.class.isAssignableFrom(type) || type == int.class;
    }

    /**
     * Checks if the given type is a {@link Byte}.
     * @param type the class to check
     * @return true if is Byte
     */
    public static boolean isByte(final Class<?> type) {
        return Byte.class.isAssignableFrom(type) || type == byte.class;
    }

    /**
     * Checks if the given type is a {@link Character}.
     * @param type the class to check
     * @return true if is Character
     */
    public static boolean isChar(final Class<?> type) {
        return Character.class.isAssignableFrom(type) || type == char.class;
    }

    /**
     * Checks if the given type is a {@link Boolean}.
     * @param type the class to check
     * @return true if is Boolean
     */
    public static boolean isBoolean(final Class<?> type) {
        return Boolean.class.isAssignableFrom(type) || type == boolean.class;
    }

    /**
     * Checks if the given type is a String.
     * @param type the class to check
     * @return true if is String
     */
    public static boolean isString(final Class<?> type) {
        return type == String.class;
    }

    /**
     * Checks if the given type is a {@link BigInteger}.
     * @param type the class to check
     * @return true if is String
     */
    public static boolean isBigInteger(final Class<?> type) {
        return type == BigInteger.class;
    }

    /**
     * Checks if the given type is a {@link BigDecimal}.
     * @param type the class to check
     * @return true if is String
     */
    public static boolean isBigDecimal(final Class<?> type) {
        return type == BigDecimal.class;
    }

    /**
     * Checks if the given type is a byte[].
     * @param type the class to check
     * @return true if is String
     */
    public static boolean isByteArray(final Class<?> type) {
        return type == byte[].class;
    }

    /**
     * Return the private final fields of a class.
     * @param clazz class from which gets the field
     * @return a list of private final fields.
     */
    @SuppressWarnings("unchecked")
    public List<Field> getPrivateFinalFields(final Class<?> clazz) {
        notNull(clazz, CLAZZ_CANNOT_BE_NULL);
        final String cacheKey = "PrivateFinalFields-" + clazz.getName();
        return CACHE_MANAGER.getFromCache(cacheKey, List.class).orElseGet(() -> {
            final List<Field> res = new ArrayList<>();
            if (hasSuperclass(clazz.getSuperclass())) {
                res.addAll(getPrivateFinalFields(clazz.getSuperclass()));
            }
            res.addAll(stream(getDeclaredFields(clazz))
                    //.parallel()
                    .filter(IS_FINAL_AND_NOT_STATIC_FIELD)
                    .collect(toList()));
            CACHE_MANAGER.cacheObject(cacheKey, res);
            return res;
        });
    }

    /**
     * Checks if a class has a clazz different from {@link Object}.
     * @param clazz the class to check.
     * @return true if the given class extends another class different from object
     */
    private boolean hasSuperclass(final Class<?> clazz) {
        return nonNull(clazz) && !clazz.equals(Object.class);
    }

    /**
     * Return the total fields matching with the given predicate.
     * @param clazz class from which gets the field
     * @param predicate the condition that needs to match
     * @return the total matching item.
     */
    public int getTotalFields(final Class<?> clazz, final Predicate<? super Field> predicate) {
        notNull(clazz, CLAZZ_CANNOT_BE_NULL);
        final String cacheKey = "TotalFields-" + clazz.getName() + '-' + predicate;
        return CACHE_MANAGER.getFromCache(cacheKey, Integer.class).orElseGet(() -> {
            List<Field> declaredFields = getDeclaredFields(clazz, true);
            int res = ofNullable(predicate)
                    .map(filter -> (int) declaredFields.stream().filter(filter).count())
                    .orElseGet(declaredFields::size);
            CACHE_MANAGER.cacheObject(cacheKey, res);
            return res;
        });
    }

    /**
     * Return the private fields of a class.
     * @param clazz class from which gets the field
     * @return a list of private final fields.
     */
    public List<Field> getPrivateFields(final Class<?> clazz) {
        return getPrivateFields(clazz, false);
    }

    /**
     * Return the private fields of a class.
     * @param clazz class from which gets the field
     * @param skipFinal if true it skips the final fields otherwise all private fields are retrieved.
     * @return a list of private fields.
     */
    @SuppressWarnings("unchecked")
    public List<Field> getPrivateFields(final Class<?> clazz, final boolean skipFinal) {
        notNull(clazz, CLAZZ_CANNOT_BE_NULL);
        final String cacheKey = "PrivateFields-" + clazz.getName() + "-skipFinal-" + skipFinal;
        return CACHE_MANAGER.getFromCache(cacheKey, List.class).orElseGet(() -> {
            final List<Field> res = new ArrayList<>();
            if (hasSuperclass(clazz.getSuperclass())) {
                res.addAll(getPrivateFields(clazz.getSuperclass(), skipFinal));
            }
            res.addAll(stream(getDeclaredFields(clazz))
                    //.parallel()
                    .filter(field -> isPrivate(field.getModifiers())
                            && (!skipFinal || !isFinal(field.getModifiers()))
                            && !isStatic(field.getModifiers()))
                    .collect(toList()));
            CACHE_MANAGER.cacheObject(cacheKey, res);
            return res;
        });
    }

    /**
     * Return the fields of a class.
     * @param clazz class from which gets the field
     * @param skipStatic if true it skips the static fields otherwise all private fields are retrieved.
     * @return a list of class fields.
     */
    @SuppressWarnings("unchecked")
    public List<Field> getDeclaredFields(final Class<?> clazz, final boolean skipStatic) {
        final String cacheKey = "DeclaredFields-" + clazz.getName() + "-skipStatic-" + skipStatic;
        return CACHE_MANAGER.getFromCache(cacheKey, List.class).orElseGet(() -> {
            final List<Field> res = new ArrayList<>();
            if (hasSuperclass(clazz.getSuperclass())) {
                res.addAll(getDeclaredFields(clazz.getSuperclass(), skipStatic));
            }
            stream(getDeclaredFields(clazz))
                    .filter(field -> !skipStatic || !isStatic(field.getModifiers()))
                    .forEach(field -> {
                        field.setAccessible(true);
                        res.add(field);
                    });
            CACHE_MANAGER.cacheObject(cacheKey, res);
            return res;
        });
    }

    /**
     * Returns the class fields.
     * @param clazz the class from which gets the field.
     * @return a list of class fields
     */
    private Field[] getDeclaredFields(final Class<?> clazz) {
        final String cacheKey = "ClassDeclaredFields-" + clazz.getName();
        return CACHE_MANAGER.getFromCache(cacheKey, Field[].class).orElseGet(() -> {
            Field[] res = clazz.getDeclaredFields();
            CACHE_MANAGER.cacheObject(cacheKey, res);
            return res;
        });
    }

    /**
     * Returns the concrete class of a field.
     * @param field the field for which the concrete class has to be retrieved.
     * @param objectInstance the object instance.
     * @param <T> the object instance class.
     * @return the concrete class of a field
     */
    public <T> Class<?> getFieldClass(final Field field, final T objectInstance) {
        return getConcreteClass(field, reflectionUtils.getFieldValue(objectInstance, field));
    }

    /**
     * Returns the concrete class of a field.
     * @param field the field for which the concrete class has to be retrieved.
     * @param fieldValue the field value.
     * @return the concrete class of a field
     */
    public Class<?> getConcreteClass(final Field field, final Object fieldValue) {
        final String cacheKey = "ConcreteFieldClass-" + field.getName() + "-" + field.getDeclaringClass();
        return CACHE_MANAGER.getFromCache(cacheKey, Class.class).orElseGet(() -> {
            Class<?> concreteType = field.getType();
            boolean isFieldValueNull = isNull(fieldValue);
            if (field.getType().isInterface()) {
                concreteType = isFieldValueNull ? Object.class : fieldValue.getClass();
            }
            if (!isFieldValueNull) {
                CACHE_MANAGER.cacheObject(cacheKey, concreteType);
            }
            return concreteType;
        });
    }

    /**
     * Checks if the destination class has accessible constructor.
     * @param targetClass the destination object class
     * @param <K> the target object type
     * @return true if the target class uses the builder pattern
     */
    public <K> boolean hasAccessibleConstructors(final Class<K> targetClass) {
        final String cacheKey = "HasAccessibleConstructors-" + targetClass.getName();
        return CACHE_MANAGER.getFromCache(cacheKey, Boolean.class).orElseGet(() -> {
            final boolean res = stream(targetClass.getDeclaredConstructors()).anyMatch(constructor -> isPublic(constructor.getModifiers()));
            CACHE_MANAGER.cacheObject(cacheKey, res);
            return res;
        });
    }

    /**
     * Retrieves all classes defined into the given one.
     * @param clazz class where we search for a nested class
     * @return all classes defined into the given one
     */
    public Class[] getDeclaredClasses(final Class<?> clazz) {
        notNull(clazz, CLAZZ_CANNOT_BE_NULL);
        String cacheKey = "DeclaredClasses-" + clazz.getName();
        return CACHE_MANAGER.getFromCache(cacheKey, Class[].class).orElseGet(() -> {
            Class[] declaredClasses = clazz.getDeclaredClasses();
            CACHE_MANAGER.cacheObject(cacheKey, declaredClasses);
            return declaredClasses;
        });
    }

    /**
     * Returns the builder class.
     * @param targetClass the class where the builder should be searched
     * @return the Builder class if available.
     */
    @SuppressWarnings("unchecked")
    public Optional<Class<?>> getBuilderClass(final Class<?> targetClass) {
        String cacheKey = "BuilderClass-" + targetClass.getName();
        return CACHE_MANAGER.getFromCache(cacheKey, Optional.class).orElseGet(() -> {
            Optional<Class> res = stream(getDeclaredClasses(targetClass))
                    .filter(nestedClass -> {
                        boolean hasBuildMethod = true;
                        try {
                            getBuildMethod(targetClass, nestedClass);
                        } catch (MissingMethodException e) {
                            hasBuildMethod = false;
                        }
                        return hasBuildMethod;
                    })
                    .findAny();
            CACHE_MANAGER.cacheObject(cacheKey, res);
            return res;
        });
    }

    /**
     * Get build method inside the Builder class.
     * @param parentClass the class containing the builder
     * @param builderClass the builder class (see Builder Pattern)
     * @return Builder build method if present
     */
    public Method getBuildMethod(final Class<?> parentClass, final Class<?> builderClass) {
        final String cacheKey = "BuildMethod-" + builderClass.getName();
        return CACHE_MANAGER.getFromCache(cacheKey, Method.class).orElseGet(() -> {
            try {
                Method method = builderClass.getDeclaredMethod(BUILD_METHOD_NAME);
                if (!method.getReturnType().equals(parentClass)) {
                    throw new MissingMethodException("Invalid " + BUILD_METHOD_NAME + " method definition. It must returns a: " + parentClass.getCanonicalName());
                }
                method.setAccessible(true);
                CACHE_MANAGER.cacheObject(cacheKey, method);
                return method;
            } catch (NoSuchMethodException e) {
                throw new MissingMethodException("No Builder " + BUILD_METHOD_NAME + " method defined for class: " + builderClass.getName() + ".");
            }
        });
    }

    /**
     * Creates an instance of the given class invoking the given constructor.
     * @param constructor the constructor to invoke.
     * @param constructorArgs the constructor args.
     * @param <T> the class object type.
     * @return the object instance.
     * @throws InstanceCreationException in case the object creation fails.
     */
    @SuppressWarnings("unchecked")
    public <T> T getInstance(final Constructor constructor, final Object... constructorArgs) {
        try {
            return (T) constructor.newInstance(constructorArgs);
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
           throw new InstanceCreationException(e.getMessage(), e);
        }
    }

    /**
     * Retrieves the no args constructor.
     * @param clazz the class from which gets the all arg constructor.
     * @param <K> the object type
     * @return the no args constructor
     * @throws InvalidBeanException if no default constructor is available
     */
    @SuppressWarnings("unchecked")
    public <K> Supplier<K> getNoArgsConstructor(final Class<K> clazz) {
        final String cacheKey = "NoArgsConstructor-" + clazz.getName();
        return CACHE_MANAGER.getFromCache(cacheKey, Supplier.class).orElseGet(() -> {
            try {
                MethodHandles.Lookup privateLookupIn = privateLookupIn(clazz, METHOD_HANDLES_LOOKUP);
                MethodHandle mh = privateLookupIn.findConstructor(clazz, methodType(void.class));
                Supplier<K> constructor = (Supplier<K>) metafactory(
                        privateLookupIn, "get", methodType(Supplier.class), mh.type().generic(), mh, mh.type()
                ).getTarget().invokeExact();
                CACHE_MANAGER.cacheObject(cacheKey, constructor);
                return constructor;
            } catch (Throwable e) {
                throw new InvalidBeanException("No default constructors available for class: " + clazz.getName());
            }
        });
    }

    /**
     * Retrieves the all args constructor.
     * @param clazz the class from which gets the all arg constructor.
     * @param <K> the object type
     * @return the all args constructor
     */
    @SuppressWarnings("unchecked")
    public <K> Constructor<K> getAllArgsConstructor(final Class<K> clazz) {
        final String cacheKey = "AllArgsConstructor-" + clazz.getName();
        return CACHE_MANAGER.getFromCache(cacheKey, Constructor.class).orElseGet(() -> {
            Constructor<?>[] declaredConstructors = clazz.getDeclaredConstructors();
            final Constructor constructor = max(asList(declaredConstructors), comparing(Constructor::getParameterCount));
            constructor.setAccessible(true);
            CACHE_MANAGER.cacheObject(cacheKey, constructor);
            return constructor;
        });
    }

    /**
     * Gets all the constructor parameters.
     * @param constructor the constructor.
     * @return the constructor parameters
     */
    public Parameter[] getConstructorParameters(final Constructor constructor) {
        final String cacheKey = "ConstructorParams-" + constructor.getDeclaringClass().getName() + '-' + constructor.getParameterCount();
        return CACHE_MANAGER.getFromCache(cacheKey, Parameter[].class).orElseGet(() -> {
            final Parameter[] parameters = constructor.getParameters();
            CACHE_MANAGER.cacheObject(cacheKey, parameters);
            return parameters;
        });
    }

    /**
     * Checks that the class has a specific field.
     * @param target the class where the field should be
     * @param fieldName the field name to retrieve
     * @return true if the field is available, false otherwise
     */
    public boolean hasField(final Object target, final String fieldName) {
        final String cacheKey = "ClassHasField-" + target.getClass().getName() + '-' + fieldName;
        return CACHE_MANAGER.getFromCache(cacheKey, Boolean.class).orElseGet(() -> {
            boolean hasField = false;
            try {
                target.getClass().getDeclaredField(fieldName);
                hasField = true;
            } catch (final NoSuchFieldException e) {
                final Class<?> superclass = target.getClass().getSuperclass();
                if (hasSuperclass(superclass)) {
                    hasField = hasField(superclass, fieldName);
                }
            }
            CACHE_MANAGER.cacheObject(cacheKey, hasField);
            return hasField;
        });
    }

    /**
     * Checks if a class has setter methods.
     * @param clazz clazz the clazz containing the methods.
     * @return true if has at least one setter method, false otherwise
     */
    public boolean hasSetterMethods(final Class<?> clazz) {
        notNull(clazz, CLAZZ_CANNOT_BE_NULL);
        final String cacheKey = "HasSetterMethods-" + clazz.getName();
        return CACHE_MANAGER.getFromCache(cacheKey, Boolean.class)
                .orElseGet(() -> {
                    final Boolean res = stream(getDeclaredMethods(clazz)).anyMatch(reflectionUtils::isSetter);
                    CACHE_MANAGER.cacheObject(cacheKey, res);
                    return res;
                });
    }

    /**
     * Retrieves the declared methods for the given class.
     * @param clazz class from which gets the methods
     * @return the class declared methods.
     */
    private Method[] getDeclaredMethods(final Class<?> clazz) {
        notNull(clazz, CLAZZ_CANNOT_BE_NULL);
        final String cacheKey = "DeclaredMethods-" + clazz.getName();
        return CACHE_MANAGER.getFromCache(cacheKey, Method[].class)
                .orElseGet(() -> {
                    final Method[] res = clazz.getDeclaredMethods();
                    CACHE_MANAGER.cacheObject(cacheKey, res);
                    return res;
                });
    }

    /**
     * Checks if a class has any final field.
     * @param clazz class from which gets the field
     * @return true if it has private final field, false otherwise.
     */
    public boolean hasFinalFields(final Class<?> clazz) {
        notNull(clazz, CLAZZ_CANNOT_BE_NULL);
        return hasFieldsMatchingCondition(clazz, IS_FINAL_AND_NOT_STATIC_FIELD, "HasFinalNotStaticFields-");
    }

    /**
     * Checks if a class has any public field.
     * @param clazz class from which gets the field
     * @return true if it has private final field, false otherwise.
     */
    private boolean hasNotFinalFields(final Class<?> clazz) {
        notNull(clazz, CLAZZ_CANNOT_BE_NULL);
        return hasFieldsMatchingCondition(clazz, IS_NOT_FINAL_AND_NOT_STATIC_FIELD, "HasNotFinalNotStaticFields-");
    }

    /**
     * Checks if a class has any field matching the given condition.
     * @param clazz class from which gets the field
     * @param filterPredicate the filter to apply
     * @param cacheKey the filter to apply
     * @return true if it has private final field, false otherwise.
     */
    private boolean hasFieldsMatchingCondition(final Class<?> clazz, final Predicate<Field> filterPredicate, final String cacheKey) {
        return CACHE_MANAGER.getFromCache(cacheKey + clazz.getName(), Boolean.class).orElseGet(() -> {
            boolean res = stream(getDeclaredFields(clazz))
                    //.parallel()
                    .anyMatch(filterPredicate);
            if (!res && nonNull(clazz.getSuperclass()) && !clazz.getSuperclass().equals(Object.class)) {
                Class<?> superclass = clazz.getSuperclass();
                res = hasFieldsMatchingCondition(superclass, filterPredicate, cacheKey);
            }
            CACHE_MANAGER.cacheObject(cacheKey + clazz.getName(), res);
            return res;
        });
    }

    /**
     * Checks if any of the class constructor's parameters are not annotated with the given class.
     * @param constructor the constructor to check.
     * @param annotationClass the annotation class to retrieve
     * @return true if any of the parameter does not contains the annotation, false otherwise.
     */
    public boolean allParameterAnnotatedWith(final Constructor constructor, final Class<? extends Annotation> annotationClass) {
        final String cacheKey = "AllParameterAnnotatedWith-" + constructor.getDeclaringClass().getName() + '-' + annotationClass.getName();
        return CACHE_MANAGER.getFromCache(cacheKey, Boolean.class).orElseGet(() -> {
            final boolean notAllAnnotatedWith = stream(constructor.getParameters())
                    .allMatch(parameter -> nonNull(parameter.getAnnotation(annotationClass)));
            CACHE_MANAGER.cacheObject(cacheKey, notAllAnnotatedWith);
            return notAllAnnotatedWith;
        });
    }

    /**
     * Checks if the constructor's parameters names are defined.
     * @param constructor the constructor to check.
     * @return true if some parameters names are not defined, false otherwise.
     */
    public boolean areParameterNamesAvailable(final Constructor constructor) {
        final String cacheKey = "AreParameterNamesAvailable-" + constructor.getDeclaringClass().getName();
        return CACHE_MANAGER.getFromCache(cacheKey, Boolean.class).orElseGet(() -> {
            final boolean res = stream(getConstructorParameters(constructor))
                    .anyMatch(Parameter::isNamePresent);
            CACHE_MANAGER.cacheObject(cacheKey, res);
            return res;
        });
    }

    /**
     * Returns the class type.
     * @param clazz the class to check
     * @return the class type {@link ClassType}
     */
    public ClassType getClassType(final Class<?> clazz) {
        final String cacheKey = "ClassType-" + clazz.getName();
        return CACHE_MANAGER.getFromCache(cacheKey, ClassType.class).orElseGet(() -> {
            final ClassType classType;
            boolean hasFinalFields = hasFinalFields(clazz);
            if (!hasFinalFields) {
                classType = MUTABLE;
            } else {
                boolean hasNotFinalFields = hasNotFinalFields(clazz);
                if (hasNotFinalFields) {
                    classType = MIXED;
                } else {
                    classType = IMMUTABLE;
                }
            }
            CACHE_MANAGER.cacheObject(cacheKey, classType);
            return classType;
        });
    }

    /**
     * Retrieves all the setters method for the given class.
     * @param clazz the class containing the methods.
     * @return all the class setter methods
     */
    public List<Method> getSetterMethods(final Class<?> clazz) {
        return getMethods(clazz, "SetterMethods", reflectionUtils::isSetter);
    }

    /**
     * Retrieves all the getters method for the given class.
     * @param clazz the class containing the methods.
     * @return all the class getter methods
     */
    public List<Method> getGetterMethods(final Class<?> clazz) {
        return getMethods(clazz, "GetterMethods", reflectionUtils::isGetter);
    }

    /**
     * Retrieves all the class methods matching to the the given filter.
     * @param clazz the class containing the methods.
     * @param cacheKeyPrefix the prefix to adopt for the cache key
     * @param methodFilter the filter to apply to the retrieved methods
     * @return the class methods matching the filter
     */
    @SuppressWarnings("unchecked")
    private List<Method> getMethods(final Class<?> clazz, final String cacheKeyPrefix, final Predicate<Method> methodFilter) {
        notNull(clazz, CLAZZ_CANNOT_BE_NULL);
        final String cacheKey = cacheKeyPrefix + "-" + clazz.getName();
        return CACHE_MANAGER.getFromCache(cacheKey, List.class).orElseGet(() -> {
            final List<Method> methods = new LinkedList<>();
            if (hasSuperclass(clazz.getSuperclass())) {
                methods.addAll(getMethods(clazz.getSuperclass(), cacheKeyPrefix, methodFilter));
            }
            methods.addAll(stream(getDeclaredMethods(clazz))
                    .filter(methodFilter)
                    .collect(toList()));
            CACHE_MANAGER.cacheObject(cacheKey, methods);
            return methods;
        });
    }

    /**
     * Gets the default value of a primitive type.
     * @param objectType the primitive object class
     * @return the default value of a primitive type
     */
    public Object getDefaultTypeValue(final Class<?> objectType) {
        final String cacheKey = "DefaultTypeValue-" + objectType.getName();
        return CACHE_MANAGER.getFromCache(cacheKey, Object.class).orElseGet(() -> {
            final Object defaultValue = isPrimitiveType(objectType) ? defaultValue(objectType) : null;
            CACHE_MANAGER.cacheObject(cacheKey, defaultValue);
            return defaultValue;
        });
    }

    /**
     * Returns all the not final fields.
     * @param clazz the class containing fields.
     * @param skipStatic if true the static fields are skipped.
     * @return a list containing all the not final fields.
     */
    @SuppressWarnings("unchecked")
    public List<Field> getNotFinalFields(final Class<?> clazz, final Boolean skipStatic) {
        final String cacheKey = "NotFinalFields-" + clazz.getName() + "-" + skipStatic;
        return CACHE_MANAGER.getFromCache(cacheKey, List.class).orElseGet(() -> {
            List<Field> notFinalFields = getDeclaredFields(clazz, skipStatic)
                    .stream()
                    .filter(IS_NOT_FINAL_FIELD).collect(toList());
            CACHE_MANAGER.cacheObject(cacheKey, notFinalFields);
            return notFinalFields;
        });
    }
}
