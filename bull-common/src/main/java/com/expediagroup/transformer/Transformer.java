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
package com.expediagroup.transformer;

import com.expediagroup.transformer.model.FieldMapping;
import com.expediagroup.transformer.model.FieldTransformer;

/**
 * Utility methods for all objects transformation.
 * @param <N> the {@link Transformer} implementation.
 */
public interface Transformer<N extends Transformer> {
    /**
     * Initializes the mapping between fields in the source object and the destination one.
     * @param fieldMapping the field mapping
     * @return the {@link Transformer} instance
     */
    N withFieldMapping(FieldMapping... fieldMapping);

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
    N withFieldTransformer(FieldTransformer... fieldTransformer);

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
