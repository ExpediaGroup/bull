/**
 * Copyright (C) 2019-2023 Expedia, Inc.
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
package com.expediagroup.beans.transformer;

import static java.util.Arrays.stream;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static java.util.Optional.empty;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.joining;
import static java.util.stream.IntStream.range;

import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

import static com.expediagroup.beans.populator.PopulatorFactory.getPopulator;
import static com.expediagroup.transformer.base.Defaults.defaultValue;
import static com.expediagroup.transformer.constant.ClassType.MIXED;
import static com.expediagroup.transformer.constant.ClassType.MUTABLE;
import static com.expediagroup.transformer.constant.Punctuation.COMMA;
import static com.expediagroup.transformer.constant.Punctuation.DOT;
import static com.expediagroup.transformer.constant.Punctuation.LPAREN;
import static com.expediagroup.transformer.constant.Punctuation.RPAREN;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Parameter;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import com.expediagroup.transformer.annotation.ConstructorArg;
import com.expediagroup.transformer.error.InvalidBeanException;
import com.expediagroup.transformer.error.MissingFieldException;
import com.expediagroup.transformer.model.FieldTransformer;

/**
 * Utility methods for populating Mutable, Immutable and Hybrid JavaBeans properties via reflection.
 * The implementations are provided by BeanUtils.
 */
public class TransformerImpl extends AbstractBeanTransformer {
    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    protected final <T, K> K transform(final T sourceObj, final Class<? extends K> targetClass, final String breadcrumb) {
        final K k;
        if (targetClass.equals(Object.class)) {
            k = (K) sourceObj;
        } else {
            final Optional<Class<?>> builderClass = getBuilderClass(targetClass);
            if (builderClass.isPresent()) {
                k = injectThroughBuilder(sourceObj, targetClass, builderClass.get(), breadcrumb);
            } else {
                k = injectValues(sourceObj, targetClass, breadcrumb);
            }
        }
        if (settings.isValidationEnabled()) {
            validator.validate(k);
        }
        return k;
    }

    /**
     * Gets the Java Bean Builder class (if any).
     * @param targetClass the destination object class
     * @param <K> the target object type
     * @return the Builder class
     */
    private <K> Optional<Class<?>> getBuilderClass(final Class<? extends K> targetClass) {
        return !settings.isCustomBuilderTransformationEnabled() ? empty() : classUtils.getBuilderClass(targetClass);
    }

    /**
     * Inject all properties value from an object to a new one using the class builder.
     * @param sourceObj the source object
     * @param targetClass the destination object class
     * @param builderClass the builder class inside the Bean
     * @param breadcrumb the full path of the current field starting from his ancestor
     * @param <T> the Source object type
     * @param <K> the target object type
     * @return a copy of the source object into the destination object
     */
    @SuppressWarnings("unchecked")
    private <T, K> K injectThroughBuilder(final T sourceObj, final Class<? extends K> targetClass, final Class<?> builderClass, final String breadcrumb) {
        return (K) reflectionUtils.invokeMethod(classUtils.getBuildMethod(targetClass, builderClass), injectValues(sourceObj, builderClass, breadcrumb));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected final <T, K> void transform(final T sourceObj, final K targetObject, final String breadcrumb) {
        injectAllFields(sourceObj, targetObject, breadcrumb);
        if (settings.isValidationEnabled()) {
            validator.validate(targetObject);
        }
    }

    /**
     * Inject all properties value from an object to a new one.
     * @param sourceObj the source object
     * @param targetClass the destination object class
     * @param breadcrumb the full path of the current field starting from his ancestor
     * @param <T> the Source object type
     * @param <K> the target object type
     * @return a copy of the source object into the destination object
     */
    private <T, K> K injectValues(final T sourceObj, final Class<? extends K> targetClass, final String breadcrumb) {
        final K k;
        final var classType = classUtils.getClassType(targetClass);
        if (classType.is(MUTABLE)) {
            try {
                k = classUtils.getNoArgsConstructor(targetClass).get();
                injectAllFields(sourceObj, k, breadcrumb);
            } catch (Exception e) {
                throw new InvalidBeanException(e.getMessage(), e);
            }
        } else {
            k = injectValues(sourceObj, targetClass, classUtils.getAllArgsConstructor(targetClass), breadcrumb, false);
            if (classType.is(MIXED)) {
                injectNotFinalFields(sourceObj, k, breadcrumb);
            }
        }
        return k;
    }

    /**
     * Inject the values through the all args constructor.
     * @param sourceObj   sourceObj the source object
     * @param targetClass the destination object class
     * @param constructor the all args constructor
     * @param breadcrumb  the full path of the current field starting from his ancestor
     * @param forceConstructorInjection if true it forces the injection trough constructor
     * @param <T>         the sourceObj object type
     * @param <K>         the target object type
     * @return a copy of the source object into the destination object
     * @throws InvalidBeanException {@link InvalidBeanException} if the target object is not compliant with the requirements
     */
    private <T, K> K injectValues(final T sourceObj, final Class<K> targetClass, final Constructor constructor, final String breadcrumb, final boolean forceConstructorInjection) {
        final Object[] constructorArgs;
        if (forceConstructorInjection || canBeInjectedByConstructorParams(constructor)) {
            constructorArgs = getConstructorArgsValues(sourceObj, targetClass, constructor, breadcrumb);
        } else {
            constructorArgs = getConstructorValuesFromFields(sourceObj, targetClass, breadcrumb);
        }
        try {
            return classUtils.getInstance(constructor, constructorArgs);
        } catch (final Exception e) {
            return handleInjectionException(sourceObj, targetClass, constructor, breadcrumb, constructorArgs, forceConstructorInjection, e);
        }
    }

    /**
     * Handles the exception thrown by method: {@code injectValues}.
     * In case an exception is raised due to missing parameter names it tries to inject through the constructor anyway.
     * @param sourceObj   sourceObj the source object
     * @param targetClass the destination object class
     * @param constructor the all args constructor
     * @param breadcrumb  the full path of the current field starting from his ancestor
     * @param constructorArgs the constructor arguments
     * @param forceConstructorInjection if true it forces the injection trough constructor
     * @param e the raised exception
     * @param <T>         the sourceObj object type
     * @param <K>         the target object type
     * @return a copy of the source object into the destination object
     * @throws InvalidBeanException {@link InvalidBeanException} if the target object is not compliant with the requirements
     */
    protected <T, K> K handleInjectionException(final T sourceObj, final Class<K> targetClass, final Constructor constructor, final String breadcrumb,
                                                final Object[] constructorArgs, final boolean forceConstructorInjection, final Exception e) {
        String errorMsg;
        if (!classUtils.areParameterNamesAvailable(constructor)) {
            if (!forceConstructorInjection) {
                return injectValues(sourceObj, targetClass, constructor, breadcrumb, true);
            } else {
                errorMsg = "Constructor's parameters name have been removed from the compiled code. "
                        + "This caused a problems during the: " + targetClass.getSimpleName() + " injection. "
                        + "Consider to use: @ConstructorArg annotation: https://github.com/ExpediaGroup/bull#different-field-names-defining-constructor-args "
                        + "or add the property: <parameters>true</parameters> to your maven-compiler configuration";
            }
        } else {
            errorMsg = "Constructor invoked with wrong arguments. Expected: " + constructor + "; Found: "
                    + getFormattedConstructorArgs(targetClass, constructorArgs)
                    + ". Double check that each " + targetClass.getSimpleName() + "'s field have the same type and name than the source object: "
                    + sourceObj.getClass().getName() + " otherwise specify a transformer configuration. Error message: " + e.getMessage();
        }
        throw new InvalidBeanException(errorMsg, e);
    }

    /**
     * Creates a string containing the arguments used to invoke the given class constructor.
     * @param targetClass the class containing the constructor
     * @param constructorArgs the passed arguments
     * @param <K> the target object type
     * @return the formatted constructor arguments
     */
    private <K> String getFormattedConstructorArgs(final Class<K> targetClass, final Object[] constructorArgs) {
        return stream(constructorArgs)
                .map(arg -> isNull(arg) ? "null" : arg.getClass().getName())
                .collect(joining(COMMA.getSymbol(), targetClass.getName() + LPAREN.getSymbol(), RPAREN.getSymbol()));
    }

    /**
     * Checks if the source class field names can be retrieved from the constructor parameters.
     * @param constructor the all args constructor
     * @return true if the parameter names are defined or the parameters are annotated with: {@link ConstructorArg}
     */
    protected boolean canBeInjectedByConstructorParams(final Constructor constructor) {
        final String cacheKey = "CanBeInjectedByConstructorParams-" + constructor.getDeclaringClass().getName();
        return cacheManager.getFromCache(cacheKey, Boolean.class).orElseGet(() -> {
            final boolean res = classUtils.areParameterNamesAvailable(constructor) || classUtils.allParameterAnnotatedWith(constructor, ConstructorArg.class);
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
     * @param breadcrumb  the full path of the current field starting from his ancestor
     * @param <T>         the sourceObj object type
     * @param <K>         the target object type
     * @return a list containing the values for the destination constructor.
     * @throws InvalidBeanException {@link InvalidBeanException} if there is an error while retrieving the constructor args parameter
     */
    protected <T, K> Object[] getConstructorArgsValues(final T sourceObj, final Class<K> targetClass, final Constructor constructor, final String breadcrumb) {
        final Parameter[] constructorParameters = classUtils.getConstructorParameters(constructor);
        final var constructorArgsValues = new Object[constructorParameters.length];
        range(0, constructorParameters.length)
                //.parallel()
                .forEach(i -> {
                    String destFieldName = getDestFieldName(constructorParameters[i], targetClass.getName());
                    if (isNull(destFieldName)) {
                        constructorArgsValues[i] = classUtils.getDefaultTypeValue(constructorParameters[i].getType());
                    } else {
                        String sourceFieldName = getSourceFieldName(destFieldName);
                        constructorArgsValues[i] =
                                getFieldValue(sourceObj, sourceFieldName, targetClass, reflectionUtils.getDeclaredField(destFieldName, targetClass), breadcrumb);
                    }
                });
        return constructorArgsValues;
    }

    /**
     * Checks if a field has to be transformed or not.
     * @param breadcrumb the field path
     * @return true if the transformation has to be applied on this field, false in it has to be skipped.
     */
    private boolean doSkipTransformation(final String breadcrumb) {
        return settings.getFieldsToSkip().contains(breadcrumb);
    }

    /**
     * Returns the field name in the source object.
     * @param field the field that has to be set.
     * @return the source field name.
     */
    private String getSourceFieldName(final Field field) {
        return getSourceFieldName(field.getName());
    }

    /**
     * Returns the field name in the source object.
     * @param fieldName the field name that has to be set.
     * @return the source field name.
     */
    private String getSourceFieldName(final String fieldName) {
        return ofNullable(settings.getFieldsNameMapping().get(fieldName)).orElse(fieldName);
    }

    /**
     * Returns the field name in the destination object.
     * @param constructorParameter the constructor parameter.
     * @param declaringClassName the class containing the constructor.
     * @return the source field name.
     */
    private String getDestFieldName(final Parameter constructorParameter, final String declaringClassName) {
        String cacheKey = "DestFieldName-" + declaringClassName + "-" + constructorParameter.getName();
        return cacheManager.getFromCache(cacheKey, String.class)
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
     * @param breadcrumb  the full path of the current field starting from his ancestor
     * @param <T>         the sourceObj object type
     * @param <K>         the target object type
     * @return a list containing the values for the destination constructor.
     * @throws InvalidBeanException {@link InvalidBeanException} if an error occurs while retrieving the value
     */
    private <T, K> Object[] getConstructorValuesFromFields(final T sourceObj, final Class<K> targetClass, final String breadcrumb) {
        final List<Field> declaredFields = classUtils.getDeclaredFields(targetClass, true);
        K k = null;
        return declaredFields.stream()
                .map(field -> getFieldValue(sourceObj, k, targetClass, field, breadcrumb))
                .toArray(Object[]::new);
    }

    /**
     * Injects the values for all class fields.
     * @param sourceObj sourceObj the source object
     * @param targetObject the destination object instance
     * @param breadcrumb  the full path of the current field starting from his ancestor
     * @param <T>  the sourceObj object type
     * @param <K> the target object type
     * @throws InvalidBeanException {@link InvalidBeanException} if an error occurs while retrieving the value
     */
    private <T, K> void injectAllFields(final T sourceObj, final K targetObject, final String breadcrumb) {
        final Class<K> targetObjectClass = (Class<K>) targetObject.getClass();
        injectFields(classUtils.getDeclaredFields(targetObjectClass, true), sourceObj, targetObject, targetObjectClass, breadcrumb);
    }

    /**
     * Creates a {@link Consumer} that sets the field value in the given class.
     * @param sourceObj sourceObj the source object
     * @param targetObject the destination object instance
     * @param targetObjectClass  the target object class
     * @param breadcrumb  the full path of the current field starting from his ancestor
     * @param <T>  the sourceObj object type
     * @param <K> the target object type
     * @return a {@link Consumer} that sets the field value in the target object
     */
    private <T, K> Consumer<Field> setFieldValue(final T sourceObj, final K targetObject, final Class<K> targetObjectClass, final String breadcrumb) {
        return field -> reflectionUtils.setFieldValue(targetObject, field, getFieldValue(sourceObj, targetObject, targetObjectClass, field, breadcrumb));
    }

    /**
     * Injects the values for all the not final fields.
     * @param sourceObj sourceObj the source object
     * @param targetObject the destination object instance
     * @param breadcrumb  the full path of the current field starting from his ancestor
     * @param <T>  the sourceObj object type
     * @param <K> the target object type
     * @throws InvalidBeanException {@link InvalidBeanException} if an error occurs while retrieving the value
     */
    private <T, K> void injectNotFinalFields(final T sourceObj, final K targetObject, final String breadcrumb) {
        final Class<K> targetObjectClass = (Class<K>) targetObject.getClass();
        injectFields(classUtils.getNotFinalFields(targetObjectClass, true), sourceObj, targetObject, targetObjectClass, breadcrumb);
    }

    /**
     * Injects the values for given final fields.
     * @param fieldList the list of field to inject
     * @param sourceObj sourceObj the source object
     * @param targetObject the destination object instance
     * @param targetObjectClass the destination object class
     * @param breadcrumb  the full path of the current field starting from his ancestor
     * @param <T>  the sourceObj object type
     * @param <K> the target object type
     * @throws InvalidBeanException {@link InvalidBeanException} if an error occurs while retrieving the value
     */
    private <T, K> void injectFields(final List<Field> fieldList, final T sourceObj, final K targetObject, final Class<K> targetObjectClass, final String breadcrumb) {
        fieldList
                //.parallelStream()
                .forEach(setFieldValue(sourceObj, targetObject, targetObjectClass, breadcrumb));
    }

    private <T, K> Object getFieldValue(final T sourceObj, final K targetObject, final Class<K> targetClass, final Field field, final String breadcrumb) {
        String sourceFieldName = getSourceFieldName(field);
        return getFieldValue(sourceObj, sourceFieldName, targetObject, targetClass, field, breadcrumb);
    }

    /**
     * Retrieves the value of a field. In case it is not a primitive type it recursively inject the values inside the object.
     * @param sourceObj sourceObj the source object
     * @param sourceFieldName sourceFieldName the field name in the source object (if different from the target one)
     * @param targetClass the destination object class
     * @param field The field for which the value has to be retrieved
     * @param breadcrumb the full path of the current field starting from his ancestor
     * @param <T> the sourceObj object type
     * @param <K> the target object type
     * @return the field value
     * @throws InvalidBeanException {@link InvalidBeanException} if an error occurs while retrieving the value
     */
    private <T, K> Object getFieldValue(final T sourceObj, final String sourceFieldName, final Class<K> targetClass, final Field field, final String breadcrumb) {
        return getFieldValue(sourceObj, sourceFieldName, null, targetClass, field, breadcrumb);
    }

    private <T, K> Object getFieldValue(final T sourceObj, final String sourceFieldName, final K targetObject,
                                        final Class<K> targetClass, final Field field, final String breadcrumb) {
        String fieldBreadcrumb = evalBreadcrumb(field.getName(), breadcrumb);
        Class<?> fieldType = field.getType();
        if (doSkipTransformation(fieldBreadcrumb)) {
            return getDefaultFieldValue(targetObject, fieldBreadcrumb, fieldType);
        }
        boolean primitiveType = classUtils.isPrimitiveType(fieldType);
        FieldTransformer transformerFunction = getTransformerFunction(field, fieldBreadcrumb);
        boolean isTransformerFunctionDefined = nonNull(transformerFunction);
        Object fieldValue = getSourceFieldValue(sourceObj, sourceFieldName, field, isTransformerFunctionDefined);
        if (nonNull(fieldValue)) {
            // is not a primitive type or an optional && there are no transformer function
            // defined it recursively evaluates the value
            boolean notPrimitiveAndNotSpecialType = !primitiveType && !classUtils.isSpecialType(fieldType);
            if (!isTransformerFunctionDefined
                    && (notPrimitiveAndNotSpecialType || Optional.class.isAssignableFrom(fieldValue.getClass()))) {
                fieldValue = getFieldValue(targetClass, field, fieldValue, fieldBreadcrumb);
            }
        } else if (primitiveType && settings.isDefaultValueForMissingPrimitiveField() && !isTransformerFunctionDefined) {
            fieldValue = defaultValue(fieldType); // assign the default value
        }
        fieldValue = getTransformedValue(transformerFunction, fieldValue, sourceObj.getClass(), sourceFieldName, field, primitiveType, fieldBreadcrumb);
        return fieldValue;
    }

    /**
     * Gets the existing value for a given field in the target object (if any) otherwise it gets the default one.
     * @param targetObject the destination object instance
     * @param fieldBreadcrumb the full path of the current field starting from his ancestor
     * @param fieldType the field class
     * @param <K> the target object type
     * @return the existing value for a given field in the target object (if any) otherwise it gets the default one.
     */
    private <K> Object getDefaultFieldValue(final K targetObject, final String fieldBreadcrumb, final Class<?> fieldType) {
        return Optional.ofNullable(targetObject)
                .map(to -> {
                    Object fieldValue;
                    try {
                        fieldValue = reflectionUtils.getFieldValue(to, fieldBreadcrumb, fieldType);
                    } catch (Exception e) {
                        fieldValue = defaultValue(fieldType);
                    }
                    return fieldValue;
                })
                .orElseGet(() -> defaultValue(fieldType));
    }

    /**
     * Applies all the transformer function associated to the field and returns the results.
     * If the field type is different and they are primitive a conversion function is automatically applied.
     * @param transformerFunction the transformer function defined for the current field
     * @param fieldValue the field value
     * @param sourceObjectClass the source object class
     * @param sourceFieldName the source field name
     * @param field the field on which the transformation should be applied
     * @param isDestinationFieldPrimitiveType indicates if the destination field type is primitive or not
     * @param breadcrumb The full field path on which the transformation should be applied
     * @return the transformer function.
     */
    @SuppressWarnings("unchecked")
    private Object getTransformedValue(final FieldTransformer transformerFunction, final Object fieldValue,
                                       final Class<?> sourceObjectClass, final String sourceFieldName, final Field field,
                                       final boolean isDestinationFieldPrimitiveType, final String breadcrumb) {
        Object transformedValue = fieldValue;
        if (settings.isPrimitiveTypeConversionEnabled() && isDestinationFieldPrimitiveType) {
            transformedValue = applyPrimitiveTypeConversion(sourceObjectClass, sourceFieldName, field, breadcrumb, transformedValue);
        }
        if (transformerFunction != null) {
            transformedValue = transformerFunction.getTransformedObject(transformedValue);
        }
        return transformedValue;
    }

    /**
     * Build the current field breadcrumb.
     * @param fieldName the field name
     * @param breadcrumb the existing breadcrumb
     * @return the updated breadcrumb
     */
    private String evalBreadcrumb(final String fieldName, final String breadcrumb) {
        return (isNotEmpty(breadcrumb) ? breadcrumb + DOT.getSymbol() : EMPTY) + fieldName;
    }

    /**
     * Gets the source field value. If a field transformer function is defined and the field does not exists in the source object it raises an exception.
     * @param sourceObj sourceObj the source object
     * @param sourceFieldName sourceFieldName the field name in the source object (if different from the target one)
     * @param field the field for which the value has to be retrieved
     * @param isFieldTransformerDefined indicates if a transformer function is implemented for this field
     * @param <T> the sourceObj object type
     * @return the source field value
     */
    private <T> Object getSourceFieldValue(final T sourceObj, final String sourceFieldName, final Field field, final boolean isFieldTransformerDefined) {
        Object fieldValue = null;
        try {
            fieldValue = reflectionUtils.getFieldValue(sourceObj, sourceFieldName, field.getType());
        } catch (MissingFieldException e) {
            // in case the source field is a primitive type and the destination one is composite, the source field value is returned without going in deep
            if (classUtils.isPrimitiveType(sourceObj.getClass())) {
                fieldValue = sourceObj;
            } else if (!isFieldTransformerDefined && !settings.isSetDefaultValueForMissingField()) {
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
     * Gets the source field type.
     * @param sourceObjectClass the source object class
     * @param sourceFieldName sourceFieldName the field name in the source object (if different from the target one)
     * @return the source field type
     */
    private Class<?> getSourceFieldType(final Class<?> sourceObjectClass, final String sourceFieldName) {
        String cacheKey = "SourceFieldType-" + sourceObjectClass.getName() + "-" + sourceFieldName;
        return cacheManager.getFromCache(cacheKey, Class.class)
                .orElseGet(() -> {
                    Class<?> classType = null;
                    try {
                        classType = reflectionUtils.getDeclaredFieldType(sourceFieldName, sourceObjectClass);
                    } catch (MissingFieldException e) {
                        // in case the source field is a primitive type and the destination one is composite,
                        // the source field type is returned without going in deep
                        if (classUtils.isPrimitiveType(sourceObjectClass)) {
                            classType = sourceObjectClass;
                        } else if (!settings.isSetDefaultValueForMissingField()) {
                            throw e;
                        }
                    }
                    cacheManager.cacheObject(cacheKey, classType);
                    return classType;
                });
    }

    /**
     * Retrieves the transformer function.
     * @param field The field on which the transformation should be applied.
     * @param breadcrumb The full field path on which the transformation should be applied.
     * @return the transformer function.
     */
    private FieldTransformer getTransformerFunction(final Field field, final String breadcrumb) {
        return settings.getFieldsTransformers().get(settings.isFlatFieldNameTransformation() ? field.getName() : breadcrumb);
    }

    /**
     * Retrieves the transformer functions. If the field type is different and they are primitive
     * a conversion function is automatically added.
     * @param sourceObjectClass the source object class
     * @param sourceFieldName the source field name
     * @param field the field on which the transformation should be applied
     * @param fieldTransformerKey the field transformation key
     * @param fieldValue the field value
     * @return the transformer function.
     */
    @SuppressWarnings("unchecked")
    private Object applyPrimitiveTypeConversion(final Class<?> sourceObjectClass, final String sourceFieldName,
                                                final Field field, final String fieldTransformerKey, final Object fieldValue) {
        Object transformedValue = fieldValue;
        FieldTransformer primitiveTypeTransformer = getPrimitiveTypeTransformer(sourceObjectClass, sourceFieldName, field, fieldTransformerKey);
        if (nonNull(primitiveTypeTransformer)) {
            transformedValue = primitiveTypeTransformer.getTransformedObject(fieldValue);
        }
        return transformedValue;
    }

    /**
     * Verifies if a default type transformer function is required and in case returns it.
     * @param sourceObjectClass the source object class
     * @param sourceFieldName the source field name
     * @param field the field on which the transformation should be applied.
     * @param fieldTransformerKey the field name or the full path to the field to which assign the transformer
     * @return the default type transformer function
     */
    private FieldTransformer getPrimitiveTypeTransformer(final Class<?> sourceObjectClass, final String sourceFieldName,
                                                         final Field field, final String fieldTransformerKey) {
        String cacheKey = TRANSFORMER_FUNCTION_CACHE_PREFIX + "-" + field.getDeclaringClass().getName() + "-" + fieldTransformerKey + "-" + field.getName();
        return cacheManager.getFromCache(cacheKey, FieldTransformer.class)
                .orElseGet(() -> {
                    FieldTransformer primitiveTypeTransformer = null;
                    Class<?> sourceFieldType = getSourceFieldType(sourceObjectClass, sourceFieldName);
                    if (nonNull(sourceFieldType)) {
                        primitiveTypeTransformer = conversionAnalyzer.getConversionFunction(sourceFieldType, field.getType())
                                .map(conversionFunction -> new FieldTransformer<>(fieldTransformerKey, conversionFunction))
                                .orElse(null);
                    }
                    cacheManager.cacheObject(cacheKey, primitiveTypeTransformer);
                    return primitiveTypeTransformer;
                });
    }

    /**
     * Retrieves the value of a field.
     * @param targetClass the destination object class
     * @param field The field for which the value has to be retrieved
     * @param fieldValue The current object value.
     * @param breadcrumb The full field path on which the transformation should be applied
     * @param <K> the target object type
     * @return the field value
     * @throws InvalidBeanException {@link InvalidBeanException} if an error occurs while retrieving the value
     */
    @SuppressWarnings("unchecked")
    private <K> Object getFieldValue(final Class<K> targetClass, final Field field, final Object fieldValue, final String breadcrumb) {
        return getPopulator(field.getType(), fieldValue.getClass(), this)
                .map(populator -> populator.getPopulatedObject(targetClass, field.getName(), fieldValue))
                .orElseGet(() ->
                        // recursively inject object
                        transform(fieldValue, classUtils.getConcreteClass(field, fieldValue), breadcrumb)
                );
    }
}
