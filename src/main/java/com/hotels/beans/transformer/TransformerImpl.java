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

import static org.apache.commons.lang3.StringUtils.EMPTY;

import static com.hotels.beans.constant.Punctuation.DOT;
import static com.hotels.beans.constant.Punctuation.COMMA;
import static com.hotels.beans.constant.Punctuation.LPAREN;
import static com.hotels.beans.constant.Punctuation.RPAREN;
import static com.hotels.beans.constant.ClassType.MIXED;
import static com.hotels.beans.constant.ClassType.MUTABLE;
import static com.hotels.beans.base.Defaults.defaultValue;
import static com.hotels.beans.populator.PopulatorFactory.getPopulator;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Parameter;
import java.util.List;
import java.util.Optional;

import com.hotels.beans.annotation.ConstructorArg;
import com.hotels.beans.constant.ClassType;
import com.hotels.beans.error.InvalidBeanException;
import com.hotels.beans.error.MissingFieldException;

/**
 * Utility methods for populating Mutable, Immutable and Hybrid JavaBeans properties via reflection.
 * The implementations are provided by BeanUtils.
 */
public class TransformerImpl extends AbstractTransformer {
    /**
     * {@inheritDoc}
     */
    @Override
    protected final <T, K> K transform(final T sourceObj, final Class<? extends K> targetClass, final String breadcrumb) {
        final K k;
        final ClassType classType = classUtils.getClassType(targetClass);
        if (classType.is(MUTABLE)) {
            try {
                k = targetClass.getDeclaredConstructor().newInstance();
                injectAllFields(sourceObj, k, breadcrumb);
            } catch (NoSuchMethodException e) {
                throw new InvalidBeanException("No default constructor defined for class: " + targetClass.getName(), e);
            } catch (Exception e) {
                throw new InvalidBeanException(e.getMessage(), e);
            }
        } else {
            k = injectValues(sourceObj, targetClass, classUtils.getAllArgsConstructor(targetClass), breadcrumb);
            if (classType.is(MIXED)) {
                injectNotFinalFields(sourceObj, k, breadcrumb);
            }
        }
        if (!settings.isValidationDisabled()) {
            validationUtils.validate(k);
        }
        return k;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected final <T, K> void transform(final T sourceObj, final K targetObject, final String breadcrumb) {
        injectAllFields(sourceObj, targetObject, breadcrumb);
        if (!settings.isValidationDisabled()) {
            validationUtils.validate(targetObject);
        }
    }

    /**
     * Inject the values through the all args constructor.
     * @param sourceObj   sourceObj the source object
     * @param targetClass the destination object class
     * @param constructor the all args constructor
     * @param breadcrumb  the full path of the current field starting from his ancestor
     * @param <T>         the sourceObj object type
     * @param <K>         the target object type
     * @return a copy of the source object into the destination object
     * @throws InvalidBeanException {@link InvalidBeanException} if the target object is not compliant with the requirements
     */
    @SuppressWarnings("unchecked")
    private <T, K> K injectValues(final T sourceObj, final Class<K> targetClass, final Constructor constructor, final String breadcrumb) {
        final Object[] constructorArgs;
        if (canBeInjectedByConstructorParams(constructor, targetClass)) {
            constructorArgs = getConstructorArgsValues(sourceObj, targetClass, constructor, breadcrumb);
        } else {
            constructorArgs = getConstructorValuesFromFields(sourceObj, targetClass, breadcrumb);
        }
        try {
            return (K) constructor.newInstance(constructorArgs);
        } catch (final Exception e) {
            throw new InvalidBeanException("Constructor invoked with arguments. Expected: " + constructor + "; Found: "
                    + getFormattedConstructorArgs(targetClass, constructorArgs)
                    + ". Double check that each " + targetClass.getSimpleName() + "'s field have the same type and name than the source object: "
                    + sourceObj.getClass().getCanonicalName() + " otherwise specify a transformer configuration. Error message: " + e.getMessage(), e);
        }
    }

    /**
     * Creates a string containing the arguments used to invoke the given class constructor.
     * @param targetClass the class containing the constructor
     * @param constructorArgs the passed arguments
     * @param <K> he target object type
     * @return the formatted constructor arguments
     */
    private <K> String getFormattedConstructorArgs(final Class<K> targetClass, final Object[] constructorArgs) {
        return stream(constructorArgs)
                .map(arg -> isNull(arg) ? "null" : arg.getClass().getCanonicalName())
                .collect(joining(COMMA.getSymbol(), targetClass.getCanonicalName() + LPAREN.getSymbol(), RPAREN.getSymbol()));
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
     * @param breadcrumb  the full path of the current field starting from his ancestor
     * @param <T>         the sourceObj object type
     * @param <K>         the target object type
     * @return a list containing the values for the destination constructor.
     * @throws InvalidBeanException {@link InvalidBeanException} if there is an error while retrieving the constructor args parameter
     */
    private <T, K> Object[] getConstructorArgsValues(final T sourceObj, final Class<K> targetClass, final Constructor constructor, final String breadcrumb) {
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
                        constructorArgsValues[i] =
                                ofNullable(getFieldValue(sourceObj, sourceFieldName, targetClass, classUtils.getDeclaredField(targetClass, destFieldName), breadcrumb))
                                .orElse(classUtils.getDefaultTypeValue(constructorParameters[i].getType()));
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
     * @param breadcrumb  the full path of the current field starting from his ancestor
     * @param <T>         the sourceObj object type
     * @param <K>         the target object type
     * @return a list containing the values for the destination constructor.
     * @throws InvalidBeanException {@link InvalidBeanException} if an error occurs while retrieving the value
     */
    private <T, K> Object[] getConstructorValuesFromFields(final T sourceObj, final Class<K> targetClass, final String breadcrumb) {
        final List<Field> declaredFields = classUtils.getDeclaredFields(targetClass, true);
        return declaredFields.stream()
                .map(field -> getFieldValue(sourceObj, targetClass, field, breadcrumb))
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
        final Class<?> targetObjectClass = targetObject.getClass();
        classUtils.getDeclaredFields(targetObjectClass, true)
                //.parallelStream()
                .forEach(field -> reflectionUtils.setFieldValue(targetObject, field, getFieldValue(sourceObj, targetObjectClass, field, breadcrumb)));
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
        final Class<?> targetObjectClass = targetObject.getClass();
        classUtils.getNotFinalFields(targetObjectClass, true)
                //.parallelStream()
                .forEach(field -> reflectionUtils.setFieldValue(targetObject, field, getFieldValue(sourceObj, targetObjectClass, field, breadcrumb)));
    }

    /**
     * Retrieves the value of a field. In case it is not a primitive type it recursively inject the values inside the object.
     * @param sourceObj sourceObj the source object
     * @param targetClass the destination object class
     * @param field The field for which the value has to be retrieved
     * @param breadcrumb  the full path of the current field starting from his ancestor
     * @param <T> the sourceObj object type
     * @param <K> the target object type
     * @return the field value
     * @throws InvalidBeanException {@link InvalidBeanException} if an error occurs while retrieving the value
     */
    private <T, K> Object getFieldValue(final T sourceObj, final Class<K> targetClass, final Field field, final String breadcrumb) {
        String sourceFieldName = getSourceFieldName(field);
        return getFieldValue(sourceObj, sourceFieldName, targetClass, field, breadcrumb);
    }

    /**
     * Retrieves the value of a field. In case it is not a primitive type it recursively inject the values inside the object.
     * @param sourceObj sourceObj the source object
     * @param sourceFieldName sourceFieldName the field name in the source object (if different from the target one)
     * @param targetClass the destination object class
     * @param field The field for which the value has to be retrieved
     * @param breadcrumb  the full path of the current field starting from his ancestor
     * @param <T> the sourceObj object type
     * @param <K> the target object type
     * @return the field value
     * @throws InvalidBeanException {@link InvalidBeanException} if an error occurs while retrieving the value
     */
    private <T, K> Object getFieldValue(final T sourceObj, final String sourceFieldName, final Class<K> targetClass, final Field field, final String breadcrumb) {
        String fieldBreadcrumb = evalBreadcrumb(field.getName(), breadcrumb);
        if (doSkipTransformation(fieldBreadcrumb)) {
            return defaultValue(field.getType());
        }
        boolean primitiveType = classUtils.isPrimitiveType(field.getType());
        boolean isFieldTransformerDefined = settings.getFieldsTransformers().containsKey(field.getName());
        Object fieldValue = getSourceFieldValue(sourceObj, sourceFieldName, field, isFieldTransformerDefined);
        if (nonNull(fieldValue)) {
            // is not a primitive type or an optional && there are no transformer function
            // defined it recursively evaluate the value
            boolean notPrimitiveAndNotSpecialType = !primitiveType && !classUtils.isSpecialType(field.getType());
            if ((notPrimitiveAndNotSpecialType || Optional.class.isAssignableFrom(fieldValue.getClass()))
                    && !isFieldTransformerDefined) {
                fieldValue = getFieldValue(targetClass, field, fieldValue, fieldBreadcrumb);
            }
        } else if (primitiveType) {
            fieldValue = defaultValue(field.getType()); // assign the default value
        }
        return getTransformedField(field, fieldBreadcrumb, fieldValue);
    }

    /**
     * Build the current field breadcrumb.
     * @param fieldName the field name
     * @param breadcrumb the existing breadcrumb
     * @return the updated breadcrumb
     */
    private String evalBreadcrumb(final String fieldName, final String breadcrumb) {
        return (nonNull(breadcrumb) ? breadcrumb + DOT.getSymbol() : EMPTY) + fieldName;
    }

    /**
     * Gets the source field value. If a field transformer function is defined and the field does not exists in the source object it raises an exception.
     * @param sourceObj sourceObj the source object
     * @param sourceFieldName sourceFieldName the field name in the source object (if different from the target one)
     * @param field The field for which the value has to be retrieved
     * @param isFieldTransformerDefined indicates if a transformer function is implemented for this field
     * @param <T> the sourceObj object type
     * @return the source field value
     */
    private <T> Object getSourceFieldValue(final T sourceObj, final String sourceFieldName, final Field field, final boolean isFieldTransformerDefined) {
        Object fieldValue = null;
        try {
            fieldValue = reflectionUtils.getFieldValue(sourceObj, sourceFieldName, field.getType());
        } catch (MissingFieldException e) {
            if (!isFieldTransformerDefined && !settings.isSetDefaultValue()) {
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
     * @param breadcrumb The full field path on which the transformation should be applied.
     * @param fieldValue The field value.
     * @return the transformed field.
     */
    private Object getTransformedField(final Field field, final String breadcrumb, final Object fieldValue) {
        String fieldName = settings.isFlatFieldNameTransformation() ? field.getName() : breadcrumb;
        return ofNullable(settings.getFieldsTransformers().get(fieldName))
                .map(fieldTransformer -> fieldTransformer.apply(fieldValue))
                .orElse(fieldValue);
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
                        transform(fieldValue, field.getType(), breadcrumb)
                );
    }
}
