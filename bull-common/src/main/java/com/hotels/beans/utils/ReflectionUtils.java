/**
 * Copyright (C) 2019 Expedia, Inc.
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

import static java.lang.invoke.LambdaMetafactory.metafactory;
import static java.lang.invoke.MethodHandles.lookup;
import static java.lang.invoke.MethodHandles.privateLookupIn;
import static java.lang.invoke.MethodType.methodType;
import static java.lang.reflect.Modifier.isPublic;

import static org.apache.commons.lang3.StringUtils.capitalize;

import static com.hotels.beans.cache.CacheManagerFactory.getCacheManager;
import static com.hotels.beans.constant.MethodPrefix.GET;
import static com.hotels.beans.constant.MethodPrefix.IS;
import static com.hotels.beans.constant.MethodPrefix.SET;

import java.lang.annotation.Annotation;
import java.lang.invoke.CallSite;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

import com.hotels.beans.cache.CacheManager;
import com.hotels.beans.error.InvalidBeanException;
import com.hotels.beans.error.MissingFieldException;
import com.hotels.beans.error.MissingMethodException;
import com.hotels.beans.model.EmptyValue;
import com.hotels.beans.model.ItemType;
import com.hotels.beans.model.MapElemType;
import com.hotels.beans.model.MapType;

/**
 * Reflection class utils.
 */
public final class ReflectionUtils {
    /**
     * boolean type name.
     */
    private static final String BOOLEAN = "boolean";

    /**
     * Regex for identify setter methods.
     */
    private static final String SETTER_METHOD_NAME_REGEX = "^set[A-Z].*";

    /**
     * Regex for identify dots into a string.
     */
    private static final String DOT_SPLIT_REGEX = "\\.";

    /**
     * Method Handles lookup.
     */
    private static final MethodHandles.Lookup METHOD_HANDLES_LOOKUP = lookup();

    /**
     * CacheManager instance {@link CacheManager}.
     */
    private static final CacheManager CACHE_MANAGER = getCacheManager("reflectionUtils");

    /**
     * Invokes the method.
     * @param method the method to be invoked.
     * @param target the class on which invoke the method
     * @param args the method parameters
     * @return the method result
     */
    protected Object invokeMethod(final Method method, final Object target, final Object... args) {
        try {
            return method.invoke(target, args);
        } catch (final Exception e) {
            handleReflectionException(e);
            throw new IllegalStateException(e);
        }
    }

    /**
     * Checks if the given method is a setter.
     * @param method the method to be checked
     * @return true if the method is a setter method, false otherwise
     */
    public boolean isSetter(final Method method) {
        final String cacheKey = "IsSetter-" + method.getDeclaringClass().getName() + '-' + method.getName();
        return CACHE_MANAGER.getFromCache(cacheKey, Boolean.class).orElseGet(() -> {
            boolean res = isPublic(method.getModifiers())
                    && method.getName().matches(SETTER_METHOD_NAME_REGEX)
                    && method.getParameterTypes().length == 1
                    && method.getReturnType().equals(void.class);
            CACHE_MANAGER.cacheObject(cacheKey, res);
            return res;
        });
    }

    /**
     * Gets the value of a field.
     * @param target the field's class
     * @param field the field {@link Field}
     * @return the field value
     */
    public Object getFieldValue(final Object target, final Field field) {
        return getFieldValue(target, field.getName(), field.getType());
    }

    /**
     * Gets the value of a field through getter method.
     * @param target the field's class
     * @param fieldName the field name
     * @param fieldType the field type
     * @return the field value
     */
    @SuppressWarnings("unchecked")
    public Object getFieldValue(final Object target, final String fieldName, final Class<?> fieldType) {
        Object fieldValue = getRealTarget(target);
        for (String currFieldName : fieldName.split(DOT_SPLIT_REGEX)) {
            if (fieldValue == null) {
                break;
            }
            try {
                fieldValue = getGetterMethodFunction(fieldValue.getClass(), currFieldName).apply(fieldValue);
            } catch (final ClassCastException | MissingMethodException | InvalidBeanException e) {
                fieldValue = getFieldValueDirectAccess(fieldValue, currFieldName);
            } catch (final MissingFieldException e) {
                fieldValue = invokeMethod(getGetterMethod(fieldValue.getClass(), currFieldName, fieldType), fieldValue);
            }
        }
        return fieldValue;
    }

    /**
     * Returns the getter method for the given field.
     * @param fieldClass the field's class
     * @param fieldName the field name
     * @param fieldType the field type
     * @return the getter method
     */
    private Method getGetterMethod(final Class<?> fieldClass, final String fieldName, final Class<?> fieldType) {
        final String cacheKey = "GetterMethod-" + fieldClass.getName() + '-' + fieldName;
        return CACHE_MANAGER.getFromCache(cacheKey, Method.class).orElseGet(() -> {
            try {
                Method method = fieldClass.getMethod(getGetterMethodPrefix(fieldType) + capitalize(fieldName));
                method.setAccessible(true);
                CACHE_MANAGER.cacheObject(cacheKey, method);
                return method;
            } catch (NoSuchMethodException e) {
                throw new MissingFieldException(fieldClass.getName() + " hasn't a field called: " + fieldName + ".");
            }
        });
    }

    /**
     * Returns the getter method for the given field.
     * @param fieldClass the field's class
     * @param fieldName the field name
     * @return the getter method
     */
    private Function getGetterMethodFunction(final Class<?> fieldClass, final String fieldName) {
        final String cacheKey = "getGetterMethodFunction-" + fieldClass.getName() + '-' + fieldName;
        return CACHE_MANAGER.getFromCache(cacheKey, Function.class).orElseGet(() -> {
            Function function;
            try {
                Class<?> fieldType = getDeclaredFieldType(fieldName, fieldClass);
                MethodHandles.Lookup privateLookupIn = privateLookupIn(fieldClass, METHOD_HANDLES_LOOKUP);
                CallSite site = metafactory(privateLookupIn,
                        "apply",
                        methodType(Function.class),
                        methodType(Object.class, Object.class),
                        privateLookupIn.findVirtual(fieldClass, getGetterMethodPrefix(fieldType) + capitalize(fieldName), methodType(fieldType)),
                        methodType(fieldType, fieldClass));
                function = (Function) site.getTarget().invokeExact();
            } catch (NoSuchFieldException | MissingFieldException e) {
                throw new MissingFieldException(e.getMessage());
            } catch (NoSuchMethodException e) {
                throw new MissingMethodException();
            } catch (Throwable e) {
                throw new InvalidBeanException(e);
            }
            CACHE_MANAGER.cacheObject(cacheKey, function);
            return function;
        });
    }

    /**
     * Returns (if existing) the field's given type annotation.
     * @param field the field that should have the annotation
     * @param annotationClazz the annotation type
     * @param <A> the annotation type object
     * @return the annotation
     */
    public <A extends Annotation> A getFieldAnnotation(final Field field, final Class<A> annotationClazz) {
        final String cacheKey = "FieldAnnotation-" + field.getDeclaringClass().getName() + "-" + field.getName() + "-" + annotationClazz.getName();
        return CACHE_MANAGER.getFromCache(cacheKey, annotationClazz).orElseGet(() -> {
            A annotation = null;
            if (field.isAnnotationPresent(annotationClazz)) {
                annotation = field.getAnnotation(annotationClazz);
            }
            CACHE_MANAGER.cacheObject(cacheKey, annotation);
            return annotation;
        });
    }

    /**
     * Returns (if existing) the constructor parameter's given type annotation.
     * @param parameter the field that should have the annotation
     * @param annotationClazz the annotation type
     * @param declaringClassName the class name that contains the parameter
     * @param <A> the annotation type object
     * @return the annotation
     */
    public <A extends Annotation> A getParameterAnnotation(final Parameter parameter, final Class<A> annotationClazz, final String declaringClassName) {
        final String cacheKey = "ParameterAnnotation-" + declaringClassName + "-" + parameter.getName() + "-" + annotationClazz.getName();
        return CACHE_MANAGER.getFromCache(cacheKey, annotationClazz).orElseGet(() -> {
            A annotation = null;
            if (parameter.isAnnotationPresent(annotationClazz)) {
                annotation = parameter.getAnnotation(annotationClazz);
            }
            CACHE_MANAGER.cacheObject(cacheKey, annotation);
            return annotation;
        });
    }

    /**
     * Gets the value of a field.
     * @param target the field's class
     * @param fieldName the field name
     * @return the field value
     */
    private Object getFieldValueDirectAccess(final Object target, final String fieldName) {
        try {
            Field field = getDeclaredField(fieldName, target.getClass());
            return field.get(target);
        } catch (MissingFieldException e) {
            throw e;
        } catch (final Exception e) {
            handleReflectionException(e);
            throw new IllegalStateException(e);
        }
    }

    /**
     * Return the field of the given class.
     * @param fieldName the name of the field to retrieve.
     * @param targetClass the field's class
     * @return the field corresponding to the given name.
     */
    public Field getDeclaredField(final String fieldName, final Class<?> targetClass) {
        final String cacheKey = "ClassDeclaredField-" + targetClass.getName() + "-" + fieldName;
        return CACHE_MANAGER.getFromCache(cacheKey, Field.class).orElseGet(() -> {
            Field field;
            try {
                field = targetClass.getDeclaredField(fieldName);
                field.setAccessible(true);
            } catch (NoSuchFieldException e) {
                Class<?> superclass = targetClass.getSuperclass();
                if (!superclass.equals(Object.class)) {
                    field = getDeclaredField(fieldName, superclass);
                } else {
                    throw new MissingFieldException(targetClass.getName() + " does not contain field: " + fieldName);
                }
            } catch (final Exception e) {
                handleReflectionException(e);
                throw new IllegalStateException(e);
            }
            CACHE_MANAGER.cacheObject(cacheKey, field);
            return field;
        });
    }

    /**
     * Return the class of the given field.
     * @param fieldName the name of the filed to retrieve.
     * @param clazz the field's class
     * @return the class field of the given field.
     */
    public Class<?> getDeclaredFieldType(final String fieldName, final Class<?> clazz) {
        final String cacheKey = "FieldType-" + clazz.getName() + "-" + fieldName;
        return CACHE_MANAGER.getFromCache(cacheKey, Class.class).orElseGet(() -> {
            Class<?> fieldType;
            Field field;
            try {
                field = clazz.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
                Class<?> superclass = clazz.getSuperclass();
                if (!superclass.equals(Object.class)) {
                    field = getDeclaredField(fieldName, superclass);
                } else {
                    throw new MissingFieldException(clazz.getName() + " does not contain field: " + fieldName);
                }
            } catch (final Exception e) {
                handleReflectionException(e);
                throw new IllegalStateException(e);
            }
            fieldType = field.getType();
            CACHE_MANAGER.cacheObject(cacheKey, fieldType);
            return fieldType;
        });
    }

    /**
     * Set the value of a field.
     * @param target the field's class
     * @param field the field to set
     * @param fieldValue the value to set
     */
    public void setFieldValue(final Object target, final Field field, final Object fieldValue) {
        try {
            setFieldValueWithoutSetterMethod(target, field, fieldValue);
        } catch (final IllegalArgumentException e) {
            throw e;
        } catch (final Exception e) {
            invokeMethod(getSetterMethodForField(target.getClass(), field.getName(), field.getType()), target, fieldValue);
        }
    }

    /**
     * Set the value of a field through {@link Field#set} method.
     * @param target the field's class
     * @param field the field to set
     * @param fieldValue the value to set
     */
    private void setFieldValueWithoutSetterMethod(final Object target, final Field field, final Object fieldValue) {
        try {
            field.set(target, fieldValue);
        } catch (final Exception e) {
            handleReflectionException(e);
            throw new IllegalStateException(e);
        }
    }

    /**
     * If an object is encapsulated into a type: {@link Optional} the contained object is returned.
     * @param target the object to check
     * @return the encapsulated object (if any)
     */
    private Object getRealTarget(final Object target) {
        AtomicReference<Object> realTarget = new AtomicReference<>(target);
        if (isOptionalType(target)) {
            ((Optional<?>) target).ifPresent(realTarget::set);
        }
        return realTarget.get();
    }

    /**
     * Checks if an object is encapsulated into a type {@link Optional}.
     * @param object the object to check
     * @return true if the given object is an Optional, false otherwise.
     */
    private boolean isOptionalType(final Object object) {
        return object instanceof Optional;
    }

    /**
     * The getter method prefix based on the field type.
     * @param fieldType the field class
     * @return the method prefix (get or is)
     */
    private String getGetterMethodPrefix(final Class<?> fieldType) {
        final String cacheKey = "GetterMethodPrefix-" + fieldType.getName();
        return CACHE_MANAGER.getFromCache(cacheKey, String.class).orElseGet(() -> {
            String res = Boolean.class.equals(fieldType) || fieldType.getName().equals(BOOLEAN) ? IS.getPrefix() : GET.getPrefix();
            CACHE_MANAGER.cacheObject(cacheKey, res);
            return res;
        });
    }

    /**
     * Gets the setter method for a specific field.
     * @param fieldClass class containing the field
     * @param fieldName the name of the field to be retrieved
     * @param fieldType the field class
     * @return the setter method
     * @throws MissingMethodException if the method does not exists
     */
    public Method getSetterMethodForField(final Class<?> fieldClass, final String fieldName, final Class<?> fieldType) {
        final String cacheKey = "SetterMethod-" + fieldClass.getName() + '-' + fieldName;
        return CACHE_MANAGER.getFromCache(cacheKey, Method.class).orElseGet(() -> {
            try {
                Method method = fieldClass.getMethod(SET.getPrefix() + capitalize(fieldName), fieldType);
                method.setAccessible(true);
                CACHE_MANAGER.cacheObject(cacheKey, method);
                return method;
            } catch (NoSuchMethodException e) {
                throw new MissingMethodException(e.getMessage());
            }
        });
    }

    /**
     * Gets the generic infer type of an object.
     * @param field the field containing the generic
     * @return the generic type class
     */
    public Class<?> getGenericFieldType(final Field field) {
        final String cacheKey = "GenericFieldType-" + field.getDeclaringClass().getName() + '-' + field.getName();
        return CACHE_MANAGER.getFromCache(cacheKey, Class.class).orElseGet(() -> {
            Class<?> res = null;
            if (ParameterizedType.class.isAssignableFrom(field.getGenericType().getClass())) {
                final Type[] fieldArgTypes = ((ParameterizedType) field.getGenericType()).getActualTypeArguments();
                if (fieldArgTypes.length != 0) {
                    res = (Class<?>) fieldArgTypes[0];
                }
            }
            CACHE_MANAGER.cacheObject(cacheKey, res);
            return res;
        });
    }

    /**
     * Gets the generic infer type of a map object.
     * @param fieldType the field containing the generic
     * @param declaringClass the class containing the field
     * @param fieldName the field name
     * @return the generic type class
     */
    public MapType getMapGenericType(final Type fieldType, final String declaringClass, final String fieldName) {
        final Class<?> fieldClass = getArgumentTypeClass(fieldType, declaringClass, fieldName, false);
        final String cacheKey = "MapGenericFieldType-" + fieldClass.getName() + '-' + fieldName;
        return CACHE_MANAGER.getFromCache(cacheKey, MapType.class).orElseGet(() -> {
            if (!Map.class.isAssignableFrom(fieldClass)) {
                throw new IllegalArgumentException("Type for object: " + fieldName + " is invalid. "
                                + "It cannot be assigned from: " + Map.class.getName() + ".");
            }
            final ParameterizedType genericType = (ParameterizedType) fieldType;
            final MapElemType keyType = getMapElemType(genericType.getActualTypeArguments()[0], declaringClass, fieldName);
            final MapElemType elemType = getMapElemType(genericType.getActualTypeArguments()[1], declaringClass, fieldName);
            final MapType mapType = new MapType(keyType, elemType);
            CACHE_MANAGER.cacheObject(cacheKey, mapType);
            return mapType;
        });
    }

    /**
     * Retrieves the generic class of the objects contained in the map.
     * It could be {@link ItemType} in case the object is primitive or is a collection
     * It could be {@link MapType} in case the object is a {@link Map}
     * @param fieldType the field containing the generic
     * @param declaringClass the class containing the field
     * @param fieldName the field name
     * @return the generic class of the objects contained in the map.
     */
    private MapElemType getMapElemType(final Type fieldType, final String declaringClass, final String fieldName) {
        final String cacheKey = "MapElemType-" + declaringClass + "-" + fieldType.getTypeName() + '-' + fieldName;
        return CACHE_MANAGER.getFromCache(cacheKey, MapElemType.class).orElseGet(() -> {
            final Class<?> argumentTypeClass = getArgumentTypeClass(fieldType, declaringClass, fieldName, false);
            final MapElemType res;
            if (Map.class.isAssignableFrom(argumentTypeClass)) {
                res = getMapGenericType(fieldType, declaringClass, fieldName);
            } else {
                res = buildItemType(fieldType, declaringClass, fieldName);
            }
            CACHE_MANAGER.cacheObject(cacheKey, res);
            return res;
        });
    }

    /**
     * Builds the {@link ItemType} object from a given object.
     * @param argument the object from which the class has to be retrieved
     * @param declaringClass the class containing the field of which the argument belongs to
     * @param fieldName the field name of which the argument belongs to
     * @return the {@link ItemType} object
     */
    private ItemType buildItemType(final Object argument, final String declaringClass, final String fieldName) {
        return ItemType.builder()
                .objectClass(getArgumentTypeClass(argument, declaringClass, fieldName, false))
                .genericClass(getArgumentTypeClass(argument, declaringClass, fieldName, true))
                .build();
    }

    /**
     * Gets the class of a given object.
     * @param argument the object from which the class has to be retrieved
     * @param declaringClass the class containing the field of which the argument belongs to
     * @param fieldName the field name of which the argument belongs to
     * @param getNestedGenericClass if true it retrieves the class of the object generic (if any). i.e. {@code argument = List<String>;}
     *                              returns {@code String}, if false returns {@code List}
     * @return the given argument class type
     */
    public Class<?> getArgumentTypeClass(final Object argument, final String declaringClass, final String fieldName, final boolean getNestedGenericClass) {
        final boolean argumentIsTypeClass = argument instanceof Class;
        final String cacheKey = "ArgumentTypeClass-" + declaringClass + "-" + fieldName + "-" + getNestedGenericClass + "-"
                + "-" + (argumentIsTypeClass || !List.class.isAssignableFrom(argument.getClass()) ? argument : argument.getClass());
        return CACHE_MANAGER.getFromCache(cacheKey, Class.class)
                .map(atc -> atc == EmptyValue.class ? null : atc)
                .orElseGet(() -> {
                    Class<?> res = null;
                    if (argumentIsTypeClass) {
                        res = getNestedGenericClass ? null : (Class<?>) argument;
                    } else if (ParameterizedType.class.isAssignableFrom(argument.getClass())) {
                        res = getNestedGenericClass
                                ? getArgumentTypeClass(((ParameterizedType) argument).getActualTypeArguments()[0], declaringClass, fieldName, false)
                                : (Class<?>) ((ParameterizedType) argument).getRawType();
                    }
                    CACHE_MANAGER.cacheObject(cacheKey, res, EmptyValue.class);
                    return res;
                });
    }

    /**
     * Gets the type of an array.
     * @param arrayField the array
     * @return the array class
     */
    public Class<?> getArrayType(final Field arrayField) {
        final String cacheKey = "ArrayType-" + arrayField.getDeclaringClass().getName();
        return CACHE_MANAGER.getFromCache(cacheKey, Class.class).orElseGet(() -> {
            final Class<?> arrayType = arrayField.getType().getComponentType();
            CACHE_MANAGER.cacheObject(cacheKey, arrayType);
            return arrayType;
        });
    }

    /**
     * Throws the right exception.
     * @param ex the exception
     */
    void handleReflectionException(final Exception ex) {
        if (ex instanceof NoSuchMethodException) {
            throw new MissingMethodException("Method not found: " + ex.getMessage());
        } else if (ex instanceof IllegalAccessException) {
            throw new IllegalStateException("Could not access method: " + ex.getMessage(), ex);
        } else {
            if (ex instanceof RuntimeException) {
                throw (RuntimeException) ex;
            } else {
                throw new UndeclaredThrowableException(ex);
            }
        }
    }
}
