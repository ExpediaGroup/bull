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

import com.expediagroup.transformer.Transformer;
import com.expediagroup.transformer.error.MissingFieldException;

/**
 * Utility methods for populating Mutable, Immutable and Hybrid JavaBeans properties via reflection.
 * The implementations are provided by {@link TransformerImpl}.
 */
public interface BeanTransformer extends Transformer<BeanTransformer> {
    /**
     * Copies all properties from an object to a new one.
     * @param sourceObj the source object
     * @param targetClass the destination object class
     * @param <T> the Source object type
     * @param <K> the target object type
     * @return a copy of the source object into the destination object
     * @throws IllegalArgumentException if any parameter is invalid
     */
    <T, K> K transform(T sourceObj, Class<? extends K> targetClass);

    /**
     * Copies all properties from an object to a new one.
     * @param sourceObj the source object
     * @param targetObject the destination object
     * @param <T> the Source object type
     * @param <K> the target object type
     * @throws IllegalArgumentException if any parameter is invalid
     */
    <T, K> void transform(T sourceObj, K targetObject);

    /**
     * It allows to configure the transformer in order to set a default value in case some field is missing in the source object.
     * If set to true the default value is set, if false if it raises a: {@link MissingFieldException} in case of missing fields.
     * @param useDefaultValue true in case the default value should be set, false if it should raise a:
     * {@link MissingFieldException} in case of missing field.
     * @return the {@link BeanTransformer} instance
     */
    BeanTransformer setDefaultValueForMissingField(boolean useDefaultValue);

    /**
     * It allows to enable/disable the set of the default value for primitive types in case they are null.
     * @param useDefaultValue if true the default value for the primitive type is set. By default it's true.
     * @return the {@link BeanTransformer} instance
     */
    BeanTransformer setDefaultValueForMissingPrimitiveField(boolean useDefaultValue);

    /**
     * It allows to configure the transformer in order to apply a transformation function on all fields matching the given name without keeping in consideration their full path.
     * If set to true the default value is set, if false if it raises a: {@link MissingFieldException} in case of missing fields.
     * @param useFlatTransformation indicates if the transformer function has to be performed on all fields matching the given name without keeping in consideration their full
     *                              path.
     * @return the {@link BeanTransformer} instance
     */
    BeanTransformer setFlatFieldNameTransformation(boolean useFlatTransformation);

    /**
     * It allows to enable the object validation.
     * @param validationEnabled if true the validation is performed.
     * @return the {@link BeanTransformer} instance
     */
    BeanTransformer setValidationEnabled(boolean validationEnabled);

    /**
     * Allows to specify all the fields for which the transformation have to be skipped.
     * @param fieldName the destination object's field(s) name that have to be skipped
     * @return the {@link BeanTransformer} instance
     */
    BeanTransformer skipTransformationForField(String... fieldName);

    /**
     * Removes all the configured fields to skip.
     */
    void resetFieldsTransformationSkip();

    /**
     * It allows to enable/disable the automatic conversion of primitive types.
     * @param primitiveTypeConversionEnabled if true primitive types are transformed automatically. By default it's false.
     * @return the {@link BeanTransformer} instance
     */
    BeanTransformer setPrimitiveTypeConversionEnabled(boolean primitiveTypeConversionEnabled);

    /**
     * It allows to enable/disable the transformation of Java Bean with a custom Builder pattern.
     * @param customBuilderEnabled if true Java Beans with a custom Builder pattern are transformed automatically. By default it's false.
     * @return the {@link BeanTransformer} instance
     */
    BeanTransformer setCustomBuilderTransformationEnabled(boolean customBuilderEnabled);
}
