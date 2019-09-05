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

package com.hotels.transformer;

import com.hotels.transformer.model.FieldMapping;
import com.hotels.transformer.model.FieldTransformer;

/**
 * Utility methods for all objects transformation.
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
     * Copies all properties from an object to a new one.
     * @param sourceObj the source object
     * @param targetObject the destination object
     * @param <T> the Source object type
     * @param <K> the target object type
     * @throws IllegalArgumentException if any parameter is invalid
     */
    <T, K> void transform(T sourceObj, K targetObject);

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
}
