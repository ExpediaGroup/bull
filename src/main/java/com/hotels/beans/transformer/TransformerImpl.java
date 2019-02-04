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

package com.hotels.beans.transformer;

import static java.util.Arrays.stream;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.joining;
import static java.util.stream.IntStream.range;

import static javax.validation.Validation.buildDefaultValidatorFactory;

import static org.apache.commons.lang3.StringUtils.SPACE;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

import static com.hotels.beans.utils.ValidationUtils.notNull;
import static com.hotels.beans.base.Defaults.defaultValue;
import static com.hotels.beans.cache.CacheManagerFactory.getCacheManager;
import static com.hotels.beans.constant.ClassType.MIXED;
import static com.hotels.beans.constant.ClassType.MUTABLE;
import static com.hotels.beans.constant.Punctuation.DOT;
import static com.hotels.beans.constant.Punctuation.SEMICOLON;
import static com.hotels.beans.populator.PopulatorFactory.getPopulator;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Parameter;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import com.hotels.beans.annotation.ConstructorArg;
import com.hotels.beans.cache.CacheManager;
import com.hotels.beans.constant.ClassType;
import com.hotels.beans.error.InvalidBeanException;
import com.hotels.beans.error.MissingFieldException;
import com.hotels.beans.model.FieldMapping;
import com.hotels.beans.model.FieldTransformer;
import com.hotels.beans.utils.ClassUtils;
import com.hotels.beans.utils.ReflectionUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * Utility methods for populating Mutable, Immutable and Hybrid JavaBeans properties via reflection.
 * The implementations are provided by BeanUtils.
 */
@Slf4j
public class TransformerImpl implements Transformer {
    /**
     * Reflection utils class {@link ReflectionUtils}.
     */
    private final ReflectionUtils reflectionUtils;

    /**
     * Class reflection utils class {@link ClassUtils}.
     */
    private final ClassUtils classUtils;

    /**
     * CacheManager class {@link CacheManager}.
     */
    private final CacheManager cacheManager;

    /**
     * Contains both the field name mapping and the lambda function to be applied on fields.
     */
    private final TransformerSettings transformerSettings;

    /**
     * Default constructor.
     */
    public TransformerImpl() {
        this.reflectionUtils = new ReflectionUtils();
        this.classUtils = new ClassUtils();
        this.transformerSettings = new TransformerSettings();
        this.cacheManager = getCacheManager("transformer");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final Transformer withFieldMapping(final FieldMapping... fieldMapping) {
        stream(fieldMapping)
                .forEach(mapping -> transformerSettings.getFieldsNameMapping().put(mapping.getDestFieldName(), mapping.getSourceFieldName()));
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void removeFieldMapping(final String destFieldName) {
        notNull(destFieldName, "The field name for which the mapping has to be removed cannot be null!");
        transformerSettings.getFieldsNameMapping().remove(destFieldName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void resetFieldsMapping() {
        transformerSettings.getFieldsNameMapping().clear();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public final Transformer withFieldTransformer(final FieldTransformer... fieldTransformer) {
        stream(fieldTransformer)
                .forEach(transformer -> transformerSettings.getFieldsTransformers().put(transformer.getDestFieldName(), transformer.getTransformerFunction()));
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void removeFieldTransformer(final String destFieldName) {
        notNull(destFieldName, "The field name for which the transformer function has to be removed cannot be null!");
        transformerSettings.getFieldsTransformers().remove(destFieldName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void resetFieldsTransformer() {
        transformerSettings.getFieldsTransformers().clear();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final Transformer setDefaultValueForMissingField(final boolean useDefaultValue) {
        transformerSettings.setSetDefaultValue(useDefaultValue);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final <T, K> K transform(final T sourceObj, final Class<? extends K> targetClass) {
        notNull(sourceObj, "The object to copy cannot be null!");
        notNull(targetClass, "The destination class cannot be null!");
        final K k;
        final ClassType classType = classUtils.getClassType(targetClass);
        if (classType.is(MUTABLE)) {
            try {
                k = targetClass.getDeclaredConstructor().newInstance();
                injectNotFinalFields(sourceObj, k);
            } catch (NoSuchMethodException e) {
                throw new InvalidBeanException("No default constructor defined for class: " + targetClass.getName(), e);
            } catch (Exception e) {
                throw new InvalidBeanException(e.getMessage(), e);
            }
        } else {
            k = injectValues(sourceObj, targetClass, classUtils.getAllArgsConstructor(targetClass));
            if (classType.is(MIXED)) {
                injectNotFinalFields(sourceObj, k);
            }
        }
        validate(k);
        return k;
    }

    /**
     * Inject the values through the all args constructor.
     * @param sourceObj   sourceObj the source object
     * @param targetClass the destination object class
     * @param constructor the all args constructor
     * @param <T>         the sourceObj object type
     * @param <K>         the target object type
     * @return a copy of the source object into the destination object
     * @throws InvalidBeanException {@link InvalidBeanException} if the target object is not compliant with the requirements
     */
    @SuppressWarnings("unchecked")
    private <T, K> K injectValues(final T sourceObj, final Class<K> targetClass, final Constructor constructor) {
        final Object[] constructorArgs;
        if (canBeInjectedByConstructorParams(constructor, targetClass)) {
            constructorArgs = getConstructorArgsValues(sourceObj, targetClass, constructor);
        } else {
            constructorArgs = getConstructorValuesFromFields(sourceObj, targetClass);
        }
        try {
            return (K) constructor.newInstance(constructorArgs);
        } catch (final Exception e) {
            throw new InvalidBeanException("Source object: " + sourceObj.getClass().getName() + "; Target class: " + targetClass.getName(), e);
        }
    }

    /**
     * Checks if the source class field names can be retrieved from the constructor parameters.
     * @param constructor the all args constructor
     * @param targetClass the destination object class
     * @param <K> the target object type
     * @return true if the parameter names are defined or the parameters are annotated with: {@link ConstructorArg}
     */
    private <K> boolean canBeInjectedByConstructorParams(final Constructor constructor, final Class<K> targetClass) {
        final String cacheKey = "CanBeInjectedByConstructorParams-" + constructor.getDeclaringClass().getCanonicalName();
        return ofNullable(cacheManager.getFromCache(cacheKey, Boolean.class)).orElseGet(() -> {
            final boolean res = classUtils.getPrivateFinalFields(targetClass).size() == constructor.getParameterCount()
                    && (classUtils.areParameterNamesAvailable(constructor) || classUtils.allParameterAnnotatedWith(constructor, ConstructorArg.class));
            cacheManager.cacheObject(cacheKey, res);
            return res;
        });
    }

    /**
     * Retrieves all the constructor argument values.
     * This methods uses the {@link ConstructorArg} to retrieve the argument values
     * @param sourceObj   sourceObj the source object
     * @param targetClass the destination object class
     * @param constructor the all args constructor
     * @param <T>         the sourceObj object type
     * @param <K>         the target object type
     * @return a list containing the values for the destination constructor.
     * @throws InvalidBeanException {@link InvalidBeanException} if there is an error while retrieving the constructor args parameter
     */
    private <T, K> Object[] getConstructorArgsValues(final T sourceObj, final Class<K> targetClass, final Constructor constructor) {
        final Parameter[] constructorParameters = classUtils.getConstructorParameters(constructor);
        final Object[] constructorArgsValues = new Object[constructorParameters.length];
        range(0, constructorParameters.length)
                //.parallel()
                .forEach(i -> {
                    String destFieldName = getDestFieldName(constructorParameters[i], targetClass.getCanonicalName());
                    if (isNull(destFieldName)) {
                        constructorArgsValues[i] =  classUtils.getDefaultTypeValue(constructorParameters[i].getType());
                    } else {
                        String sourceFieldName = getSourceFieldName(destFieldName);
                        constructorArgsValues[i] = ofNullable(getFieldValue(sourceObj, sourceFieldName, targetClass, classUtils.getDeclaredField(targetClass, destFieldName)))
                                .orElse(classUtils.getDefaultTypeValue(constructorParameters[i].getType()));
                    }
                });
        return constructorArgsValues;
    }

    /**
     * Returns the field name in the source object.
     * @param field the field that has to be valorized.
     * @return the source field name.
     */
    private String getSourceFieldName(final Field field) {
        return getSourceFieldName(field.getName());
    }

    /**
     * Returns the field name in the source object.
     * @param fieldName the field name that has to be valorized.
     * @return the source field name.
     */
    private String getSourceFieldName(final String fieldName) {
        return ofNullable(transformerSettings.getFieldsNameMapping().get(fieldName)).orElse(fieldName);
    }

    /**
     * Returns the field name in the destination object.
     * @param constructorParameter the constructor parameter.
     * @param declaringClassName the class containing the constructor.
     * @return the source field name.
     */
    private String getDestFieldName(final Parameter constructorParameter, final String declaringClassName) {
        String cacheKey = "DestFieldName-" + declaringClassName + "-" + constructorParameter.getName();
        return ofNullable(cacheManager.getFromCache(cacheKey, String.class))
                .orElseGet(() -> {
                    String destFieldName;
                    if (constructorParameter.isNamePresent()) {
                        destFieldName = constructorParameter.getName();
                    } else {
                        destFieldName = ofNullable(reflectionUtils.getParameterAnnotation(constructorParameter, ConstructorArg.class, declaringClassName))
                                .map(ConstructorArg::value)
                                .orElse(null);
                    }
                    cacheManager.cacheObject(cacheKey, destFieldName);
                    return destFieldName;
                });
    }

    /**
     * Retrieves all the constructor argument values.
     * This methods retrieves the values from the declared class field into the target object.
     * @param sourceObj   sourceObj the source object
     * @param targetClass the destination object class
     * @param <T>         the sourceObj object type
     * @param <K>         the target object type
     * @return a list containing the values for the destination constructor.
     * @throws InvalidBeanException {@link InvalidBeanException} if an error occurs while retrieving the value
     */
    private <T, K> Object[] getConstructorValuesFromFields(final T sourceObj, final Class<K> targetClass) {
        final List<Field> declaredFields = classUtils.getDeclaredFields(targetClass, true);
        return declaredFields.stream()
                .map(field -> getFieldValue(sourceObj, targetClass, field))
                .toArray(Object[]::new);
    }

    /**
     * Retrieves all the constructor argument values.
     * This methods retrieves the values from the declared class field into the target object.
     * @param sourceObj sourceObj the source object
     * @param targetObject the destination object instance
     * @param <T>  the sourceObj object type
     * @param <K> the target object type
     * @throws InvalidBeanException {@link InvalidBeanException} if an error occurs while retrieving the value
     */
    private <T, K> void injectNotFinalFields(final T sourceObj, final K targetObject) {
        final Class<?> targetObjectClass = targetObject.getClass();
        classUtils.getNotFinalFields(targetObjectClass, true)
                //.parallelStream()
                .forEach(field -> reflectionUtils.setFieldValue(targetObject, field, getFieldValue(sourceObj, targetObjectClass, field)));
    }

    /**
     * Retrieves the value of a field. In case it is not a primitive type it recursively inject the values inside the object.
     * @param sourceObj sourceObj the source object
     * @param targetClass the destination object class
     * @param field The field for which the value has to be retrieved
     * @param <T> the sourceObj object type
     * @param <K> the target object type
     * @return the field value
     * @throws InvalidBeanException {@link InvalidBeanException} if an error occurs while retrieving the value
     */
    private <T, K> Object getFieldValue(final T sourceObj, final Class<K> targetClass, final Field field) {
        String sourceFieldName = getSourceFieldName(field);
        return getFieldValue(sourceObj, sourceFieldName, targetClass, field);
    }

    /**
     * Retrieves the value of a field. In case it is not a primitive type it recursively inject the values inside the object.
     * @param sourceObj sourceObj the source object
     * @param sourceFieldName sourceFieldName the field name in the source object (if different from the target one)
     * @param targetClass the destination object class
     * @param field The field for which the value has to be retrieved
     * @param <T> the sourceObj object type
     * @param <K> the target object type
     * @return the field value
     * @throws InvalidBeanException {@link InvalidBeanException} if an error occurs while retrieving the value
     */
    private <T, K> Object getFieldValue(final T sourceObj, final String sourceFieldName, final Class<K> targetClass, final Field field) {
        Object fieldValue = null;
        if (isNotEmpty(sourceFieldName)) {
            boolean primitiveType = classUtils.isPrimitiveType(field.getType());
            boolean isFieldTransformerDefined = transformerSettings.getFieldsTransformers().containsKey(field.getName());
            fieldValue = getSourceFieldValue(sourceObj, sourceFieldName, field, isFieldTransformerDefined);
            if (nonNull(fieldValue)) {
                // is not a primitive type or an optional && there are no transformer function
                // defined it recursively evaluate the value
                boolean notPrimitiveAndNotSpecialType = !primitiveType && !classUtils.isSpecialType(field.getType());
                if ((notPrimitiveAndNotSpecialType || Optional.class.isAssignableFrom(fieldValue.getClass()))
                        && !isFieldTransformerDefined) {
                    fieldValue = getFieldValue(targetClass, field, fieldValue);
                }
            } else if (primitiveType) {
                fieldValue = defaultValue(field.getType()); // assign the default value
            }
        }
        return getTransformedField(field, fieldValue);
    }

    /**
     * Gets the source field value. If a field transformer function is defined and the field does not exists in the source object it raises an exception.
     * @param sourceObj sourceObj the source object
     * @param sourceFieldName sourceFieldName the field name in the source object (if different from the target one)
     * @param field The field for which the value has to be retrieved
     * @param isFieldTransformerDefined indicates if a tranformer function is implemented for this field
     * @param <T> the sourceObj object type
     * @return the source field value
     */
    private <T> Object getSourceFieldValue(final T sourceObj, final String sourceFieldName, final Field field, final boolean isFieldTransformerDefined) {
        Object fieldValue = null;
        try {
            fieldValue = reflectionUtils.getFieldValue(sourceObj, sourceFieldName, field.getType());
        } catch (MissingFieldException e) {
            if (!isFieldTransformerDefined && !transformerSettings.isSetDefaultValue()) {
                throw e;
            }
        } catch (Exception e) {
            if (!isFieldTransformerDefined) {
                throw e;
            }
        }
        return fieldValue;
    }

    /**
     * It executes the lambda function defined to the field.
     * @param field The field on which the transformation should be applied.
     * @param fieldValue The field value.
     * @return the transformed field.
     */
    private Object getTransformedField(final Field field, final Object fieldValue) {
        return ofNullable(transformerSettings.getFieldsTransformers().get(field.getName()))
                .map(fieldTransformer -> fieldTransformer.apply(fieldValue))
                .orElse(fieldValue);
    }

    /**
     * Retrieves the value of a field.
     * @param targetClass the destination object class
     * @param field The field for which the value has to be retrieved
     * @param fieldValue The current object value.
     * @param <K> the target object type
     * @return the field value
     * @throws InvalidBeanException {@link InvalidBeanException} if an error occurs while retrieving the value
     */
    @SuppressWarnings("unchecked")
    private <K> Object getFieldValue(final Class<K> targetClass, final Field field, final Object fieldValue) {
        return getPopulator(field.getType(), fieldValue.getClass(), this)
                .map(populator -> populator.getPopulatedObject(targetClass, field.getName(), fieldValue))
                .orElseGet(() ->
                        // recursively inject object
                        transform(fieldValue, field.getType())
                );
    }

    /**
     * Checks if an object is valid.
     * @param k the object to check
     * @param <K> the object class
     * @throws InvalidBeanException {@link InvalidBeanException} if the validation fails
     */
    private <K> void validate(final K k) {
        final Set<ConstraintViolation<Object>> constraintViolations = getValidator().validate(k);
        if (!constraintViolations.isEmpty()) {
            final String errors = constraintViolations.stream()
                    .map(cv -> cv.getRootBeanClass().getCanonicalName()
                            + DOT.getSymbol()
                            + cv.getPropertyPath()
                            + SPACE
                            + cv.getMessage())
                    .collect(joining(SEMICOLON.getSymbol()));
            throw new InvalidBeanException(errors);
        }
    }

    /**
     * Creates the validator.
     * @return a {@link Validator} instance.
     */
    private Validator getValidator() {
        String cacheKey = "BeanValidator";
        return ofNullable(cacheManager.getFromCache(cacheKey, Validator.class))
                .orElseGet(() -> {
                    Validator validator = buildDefaultValidatorFactory().getValidator();
                    cacheManager.cacheObject(cacheKey, validator);
                    return validator;
                });
    }
}
