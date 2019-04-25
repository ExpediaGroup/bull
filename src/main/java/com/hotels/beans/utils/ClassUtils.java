/**
 * Copyright (C) 2019 Expedia Inc.
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

package com.hotels.beans.utils;

import static java.lang.reflect.Modifier.isFinal;
import static java.lang.reflect.Modifier.isPrivate;
import static java.lang.reflect.Modifier.isPublic;
import static java.lang.reflect.Modifier.isStatic;
import static java.util.Arrays.asList;
import static java.util.Arrays.stream;
import static java.util.Collections.max;
import static java.util.Collections.min;
import static java.util.Comparator.comparing;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;

import static com.hotels.beans.utils.ValidationUtils.notNull;
import static com.hotels.beans.base.Defaults.defaultValue;
import static com.hotels.beans.cache.CacheManagerFactory.getCacheManager;
import static com.hotels.beans.constant.ClassType.IMMUTABLE;
import static com.hotels.beans.constant.ClassType.MIXED;
import static com.hotels.beans.constant.ClassType.MUTABLE;
import static com.hotels.beans.constant.Filters.IS_NOT_FINAL_FIELD;
import static com.hotels.beans.constant.Filters.IS_FINAL_AND_NOT_STATIC_FIELD;
import static com.hotels.beans.constant.Filters.IS_NOT_FINAL_AND_NOT_STATIC_FIELD;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.Currency;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.function.Predicate;
import java.util.stream.Stream;

import com.hotels.beans.cache.CacheManager;
import com.hotels.beans.constant.ClassType;
import com.hotels.beans.error.InvalidBeanException;

/**
 * Reflection utils for Class objects.
 */
public final class ClassUtils {
    /**
     * Class nullability error message constant.
     */
    private static final String CLAZZ_CANNOT_BE_NULL = "clazz cannot be null!";

    /**
     * Reflection utils instance {@link ReflectionUtils}.
     */
    private final ReflectionUtils reflectionUtils;

    /**
     * CacheManager class {@link CacheManager}.
     */
    private final CacheManager cacheManager;

    /**
     * Default constructor.
     */
    public ClassUtils() {
        this.reflectionUtils = new ReflectionUtils();
        this.cacheManager = getCacheManager("classUtils");
    }

    /**
     * Checks if an object is a primitive or special type.
     * @param clazz the class to check
     * @return true if is primitive or special type, false otherwise
     */
    public boolean isPrimitiveOrSpecialType(final Class<?> clazz) {
        final String cacheKey = "isPrimitiveOrSpecial-" + clazz.getName();
        return cacheManager.getFromCache(cacheKey, Boolean.class).orElseGet(() -> {
            final Boolean res = isPrimitiveType(clazz) || isSpecialType(clazz);
            cacheManager.cacheObject(cacheKey, res);
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
        return cacheManager.getFromCache(cacheKey, Boolean.class).orElseGet(() -> {
            final Boolean res = clazz.isPrimitive() || clazz.equals(String.class) || clazz.isEnum() || Number.class.isAssignableFrom(clazz);
            cacheManager.cacheObject(cacheKey, res);
            return res;
        });
    }

    /**
     * Checks if an object is a primitive type array.
     * @param object the object to check
     * @return true if is primitive type array, false otherwise
     */
    public boolean isPrimitiveTypeArray(final Object object) {
        final String cacheKey = "isPrimitiveTypeArray-" + object.getClass().getName();
        return cacheManager.getFromCache(cacheKey, Boolean.class).orElseGet(() -> {
            final Boolean res = object instanceof int[] || object instanceof char[] || object instanceof short[]
                    || object instanceof long[] || object instanceof byte[] || object instanceof float[] || object instanceof double[];
            cacheManager.cacheObject(cacheKey, res);
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
        return cacheManager.getFromCache(cacheKey, Boolean.class).orElseGet(() -> {
            final Boolean res = clazz.equals(Currency.class) || clazz.equals(Locale.class) || Temporal.class.isAssignableFrom(clazz)
                    || clazz.isSynthetic() || clazz.isAnonymousClass();
            cacheManager.cacheObject(cacheKey, res);
            return res;
        });
    }

    /**
     * Checks if the given type is a Double.
     * @param type the class to check
     * @return true if is Double
     */
    public static boolean isDouble(final Class<?> type) {
        return Double.class.isAssignableFrom(type) || type == double.class;
    }

    /**
     * Checks if the given type is a Float.
     * @param type the class to check
     * @return true if is Float
     */
    public static boolean isFloat(final Class<?> type) {
        return Float.class.isAssignableFrom(type) || type == float.class;
    }

    /**
     * Checks if the given type is a Long.
     * @param type the class to check
     * @return true if is Long
     */
    public static boolean isLong(final Class<?> type) {
        return Long.class.isAssignableFrom(type) || type == long.class;
    }

    /**
     * Checks if the given type is a Short.
     * @param type the class to check
     * @return true if is Short
     */
    public static boolean isShort(final Class<?> type) {
        return Short.class.isAssignableFrom(type) || type == short.class;
    }

    /**
     * Checks if the given type is a Integer.
     * @param type the class to check
     * @return true if is Integer
     */
    public static boolean isInt(final Class<?> type) {
        return Integer.class.isAssignableFrom(type) || type == int.class;
    }

    /**
     * Checks if the given type is a Byte.
     * @param type the class to check
     * @return true if is Byte
     */
    public static boolean isByte(final Class<?> type) {
        return Byte.class.isAssignableFrom(type) || type == byte.class;
    }

    /**
     * Checks if the given type is a Boolean.
     * @param type the class to check
     * @return true if is Character
     */
    public static boolean isChar(final Class<?> type) {
        return Character.class.isAssignableFrom(type) || type == char.class;
    }

    /**
     * Checks if the given type is a Boolean.
     * @param type the class to check
     * @return true if is Boolean
     */
    public static boolean isBoolean(final Class<?> type) {
        return Boolean.class.isAssignableFrom(type) || type == boolean.class;
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
        return cacheManager.getFromCache(cacheKey, List.class).orElseGet(() -> {
            final List<Field> res = new ArrayList<>();
            if (nonNull(clazz.getSuperclass()) && !clazz.getSuperclass().equals(Object.class)) {
                res.addAll(getPrivateFinalFields(clazz.getSuperclass()));
            }
            res.addAll(stream(getDeclaredFields(clazz))
                    //.parallel()
                    .filter(IS_FINAL_AND_NOT_STATIC_FIELD)
                    .collect(toList()));
            cacheManager.cacheObject(cacheKey, res);
            return res;
        });
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
        return cacheManager.getFromCache(cacheKey, Integer.class).orElseGet(() -> {
            List<Field> declaredFields = getDeclaredFields(clazz, true);
            int res = ofNullable(predicate)
                    .map(filter -> (int) declaredFields.stream().filter(filter).count())
                    .orElseGet(declaredFields::size);
            cacheManager.cacheObject(cacheKey, res);
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
        return cacheManager.getFromCache(cacheKey, List.class).orElseGet(() -> {
            final List<Field> res = new ArrayList<>();
            if (nonNull(clazz.getSuperclass()) && !clazz.getSuperclass().equals(Object.class)) {
                res.addAll(getPrivateFields(clazz.getSuperclass(), skipFinal));
            }
            res.addAll(stream(getDeclaredFields(clazz))
                    //.parallel()
                    .filter(field -> isPrivate(field.getModifiers())
                            && (!skipFinal || !isFinal(field.getModifiers()))
                            && !isStatic(field.getModifiers()))
                    .collect(toList()));
            cacheManager.cacheObject(cacheKey, res);
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
        return cacheManager.getFromCache(cacheKey, List.class).orElseGet(() -> {
            final List<Field> res = new LinkedList<>();
            if (nonNull(clazz.getSuperclass()) && !clazz.getSuperclass().equals(Object.class)) {
                res.addAll(getDeclaredFields(clazz.getSuperclass(), skipStatic));
            }
            Stream<Field> fieldStream = stream(getDeclaredFields(clazz));
            if (skipStatic) {
//                fieldStream = fieldStream.parallel().filter(field -> !isStatic(field.getModifiers()));
                fieldStream = fieldStream.filter(field -> !isStatic(field.getModifiers()));
            }
            res.addAll(fieldStream.collect(toList()));
            cacheManager.cacheObject(cacheKey, res);
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
        return cacheManager.getFromCache(cacheKey, Field[].class).orElseGet(() -> {
            Field[] res = clazz.getDeclaredFields();
            cacheManager.cacheObject(cacheKey, res);
            return res;
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
        return cacheManager.getFromCache(cacheKey, Boolean.class).orElseGet(() -> {
            final boolean res = stream(targetClass.getDeclaredConstructors()).anyMatch(constructor -> isPublic(constructor.getModifiers()));
            cacheManager.cacheObject(cacheKey, res);
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
        return cacheManager.getFromCache(cacheKey, Class[].class).orElseGet(() -> {
            Class[] declaredClasses = clazz.getDeclaredClasses();
            cacheManager.cacheObject(cacheKey, declaredClasses);
            return declaredClasses;
        });
    }

    /**
     * Creates an instance of the given class invoking the no args constructor.
     * @param objectClass the class of the object to return.
     * @param <T> the class object type.
     * @return the object instance.
     * @throws InvalidBeanException in case the object creation fails.
     */
    public <T> T getInstance(final Class<? extends T> objectClass) {
        try {
            return getInstance(getNoArgsConstructor(objectClass));
        } catch (final NoSuchMethodException e) {
            throw new InvalidBeanException("No default constructor defined for class: " + objectClass.getName(), e);
        } catch (final Exception e) {
            throw new InvalidBeanException(e.getMessage(), e);
        }
    }

    /**
     * Creates an instance of the given class invoking the given constructor.
     * @param constructor the constructor to invoke.
     * @param constructorArgs the constructor args.
     * @param <T> the class object type.
     * @return the object instance.
     * @throws Exception in case the object creation fails.
     */
    @SuppressWarnings("unchecked")
    public <T> T getInstance(final Constructor constructor, final Object... constructorArgs) throws Exception {
        boolean isAccessible = reflectionUtils.isAccessible(constructor, null);
        try {
            if (!isAccessible) {
                constructor.setAccessible(true);
            }
            return (T) constructor.newInstance(constructorArgs);
        } finally {
            if (!isAccessible) {
                constructor.setAccessible(false);
            }
        }
    }

    /**
     * Retrieves the no args constructor.
     * @param clazz the class from which gets the all arg constructor.
     * @param <K> the object type
     * @return the no args constructor
     */
    public <K> Constructor getNoArgsConstructor(final Class<K> clazz) {
        notNull(clazz, CLAZZ_CANNOT_BE_NULL);
        final String cacheKey = "NoArgsConstructor-" + clazz.getName();
        return cacheManager.getFromCache(cacheKey, Constructor.class).orElseGet(() -> {
            Constructor<?>[] declaredConstructors = clazz.getDeclaredConstructors();
            final Constructor constructor = min(asList(declaredConstructors), comparing(Constructor::getParameterCount));
            if (constructor.getParameterCount() != 0) {
                throw new InvalidBeanException("No default constructors available");
            }
            cacheManager.cacheObject(cacheKey, constructor);
            return constructor;
        });
    }

    /**
     * Retrieves the all args constructor.
     * @param clazz the class from which gets the all arg constructor.
     * @param <K> the object type
     * @return the all args constructor
     */
    public <K> Constructor getAllArgsConstructor(final Class<K> clazz) {
        notNull(clazz, CLAZZ_CANNOT_BE_NULL);
        final String cacheKey = "AllArgsConstructor-" + clazz.getName();
        return cacheManager.getFromCache(cacheKey, Constructor.class).orElseGet(() -> {
            Constructor<?>[] declaredConstructors = clazz.getDeclaredConstructors();
            final Constructor constructor = max(asList(declaredConstructors), comparing(Constructor::getParameterCount));
            cacheManager.cacheObject(cacheKey, constructor);
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
        return cacheManager.getFromCache(cacheKey, Parameter[].class).orElseGet(() -> {
            final Parameter[] parameters = constructor.getParameters();
            cacheManager.cacheObject(cacheKey, parameters);
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
        return cacheManager.getFromCache(cacheKey, Boolean.class).orElseGet(() -> {
            boolean hasField;
            try {
                hasField = nonNull(target.getClass().getDeclaredField(fieldName));
            } catch (final NoSuchFieldException e) {
                hasField = false;
                final Class<?> superclass = target.getClass().getSuperclass();
                if (nonNull(superclass) && !superclass.equals(Object.class)) {
                    hasField = hasField(superclass, fieldName);
                }
            }
            cacheManager.cacheObject(cacheKey, hasField);
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
        return cacheManager.getFromCache(cacheKey, Boolean.class)
                .orElseGet(() -> {
                    final Boolean res = stream(getDeclaredMethods(clazz)).anyMatch(reflectionUtils::isSetter);
                    cacheManager.cacheObject(cacheKey, res);
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
        return cacheManager.getFromCache(cacheKey, Method[].class)
                .orElseGet(() -> {
                    final Method[] res = clazz.getDeclaredMethods();
                    cacheManager.cacheObject(cacheKey, res);
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
        return cacheManager.getFromCache(cacheKey + clazz.getName(), Boolean.class).orElseGet(() -> {
            boolean res = stream(getDeclaredFields(clazz))
                    //.parallel()
                    .anyMatch(filterPredicate);
            if (!res && nonNull(clazz.getSuperclass()) && !clazz.getSuperclass().equals(Object.class)) {
                Class<?> superclass = clazz.getSuperclass();
                res = hasFieldsMatchingCondition(superclass, filterPredicate, cacheKey);
            }
            cacheManager.cacheObject(cacheKey + clazz.getName(), res);
            return res;
        });
    }

    /**
     * Checks if the class constructor's parameters are annotated with the given class.
     * @param constructor the constructor to check.
     * @param annotationClass the annotation class to retrieve
     * @return true if any of the parameter contains the annotation, false otherwise.
     */
    public boolean containsAnnotation(final Constructor constructor, final Class<? extends Annotation> annotationClass) {
        final String cacheKey = "ConstructorHasAnnotation-" + constructor.getDeclaringClass().getName() + '-' + annotationClass.getName();
        return cacheManager.getFromCache(cacheKey, Boolean.class).orElseGet(() -> {
            final boolean containsAnnotation = stream(constructor.getParameters())
                    .noneMatch(parameter -> isNull(parameter.getAnnotation(annotationClass)));
            cacheManager.cacheObject(cacheKey, containsAnnotation);
            return containsAnnotation;
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
        return cacheManager.getFromCache(cacheKey, Boolean.class).orElseGet(() -> {
            final boolean notAllAnnotatedWith = stream(constructor.getParameters())
                    .allMatch(parameter -> nonNull(parameter.getAnnotation(annotationClass)));
            cacheManager.cacheObject(cacheKey, notAllAnnotatedWith);
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
        return cacheManager.getFromCache(cacheKey, Boolean.class).orElseGet(() -> {
            final boolean res = stream(getConstructorParameters(constructor))
                    .anyMatch(Parameter::isNamePresent);
            cacheManager.cacheObject(cacheKey, res);
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
        return cacheManager.getFromCache(cacheKey, ClassType.class).orElseGet(() -> {
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
            cacheManager.cacheObject(cacheKey, classType);
            return classType;
        });
    }

    /**
     * Retrieves all the setters method for the given class.
     * @param clazz the clazz containing the methods.
     * @return all the class setter methods
     */
    @SuppressWarnings("unchecked")
    public List<Method> getSetterMethods(final Class<?> clazz) {
        notNull(clazz, CLAZZ_CANNOT_BE_NULL);
        final String cacheKey = "SetterMethods-" + clazz.getName();
        return cacheManager.getFromCache(cacheKey, List.class).orElseGet(() -> {
            final List<Method> setterMethods = new LinkedList<>();
            if (nonNull(clazz.getSuperclass()) && !clazz.getSuperclass().equals(Object.class)) {
                setterMethods.addAll(getSetterMethods(clazz.getSuperclass()));
            }
            setterMethods.addAll(stream(getDeclaredMethods(clazz))
                    .filter(reflectionUtils::isSetter)
                    .collect(toList()));
            cacheManager.cacheObject(cacheKey, setterMethods);
            return setterMethods;
        });
    }

    /**
     * Gets the default value of a primitive type.
     * @param objectType the primitive object class
     * @return the default value of a primitive type
     */
    public Object getDefaultTypeValue(final Class<?> objectType) {
        final String cacheKey = "DefaultTypeValue-" + objectType.getName();
        return cacheManager.getFromCache(cacheKey, Object.class).orElseGet(() -> {
            final Object defaultValue = isPrimitiveType(objectType) ? defaultValue(objectType) : null;
            cacheManager.cacheObject(cacheKey, defaultValue);
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
        return cacheManager.getFromCache(cacheKey, List.class).orElseGet(() -> {
            List<Field> notFinalFields = getDeclaredFields(clazz, skipStatic)
                    .stream()
                    .filter(IS_NOT_FINAL_FIELD).collect(toList());
            cacheManager.cacheObject(cacheKey, notFinalFields);
            return notFinalFields;
        });
    }
}
