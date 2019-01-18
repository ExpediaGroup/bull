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

import com.hotels.beans.model.FieldMapping;
import com.hotels.beans.model.FieldTransformer;

/**
 * Utility methods for populating Mutable, Immutable and Hybrid JavaBeans properties via reflection.
 * The implementations are provided by BeanUtils.
 */
public interface Transformer {
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
     * Initializes the mapping between fields in the source object and the destination one.
     * @param fieldMapping the field mapping
     * @return the {@link Transformer} instance
     */
    Transformer withFieldMapping(FieldMapping... fieldMapping);

    /**
     * Removes the field mapping for the given field.
     * @param destFieldName the field name in the destination object
     */
    void removeFieldMapping(String destFieldName);

    /**
     * Removes all the configured fields mapping.
     */
    void resetFieldsMapping();

    /**
     * Initializes the field transformer functions. The transformer function returns directly the field value.
     * @param fieldTransformer the fields transformer function
     * @return the {@link Transformer} instance
     */
    Transformer withFieldTransformer(FieldTransformer... fieldTransformer);

    /**
     * Removes the field transformer for the given field.
     * @param destFieldName the field name in the destination object
     */
    void removeFieldTransformer(String destFieldName);

    /**
     * Removes all the configured fields transformer.
     */
    void resetFieldsTransformer();

    /**
     * It allows to configure the transformer in order to set a default value in case some field is missing in the source object.
     * If set to true the default value is set, if false if it raises a: {@link com.hotels.beans.error.MissingFieldException} in case of missing fields.
     * @param useDefaultValue true in case the default value should be set, false if it should raise a: {@link com.hotels.beans.error.MissingFieldException} in case of missing
     *                        field.
     * @return the {@link Transformer} instance
     */
    Transformer setDefaultValueForMissingField(boolean useDefaultValue);
}
