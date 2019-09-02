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

package com.hotels.beans.transformer;

import com.hotels.beans.model.FieldMapping;
import com.hotels.beans.model.FieldTransformer;

/**
 * Utility methods for all objects transformation.
 * @deprecated This class will be removed since version 1.1.25 please use {@link com.hotels.transformer.Transformer} instead.
 */
@Deprecated
public interface Transformer extends com.hotels.transformer.Transformer {
    /**
     * Initializes the mapping between fields in the source object and the destination one.
     * @param fieldMapping the field mapping
     * @return the {@link Transformer} instance
     * @deprecated use {@code withFieldMapping(com.hotels.transformer.model.FieldMapping)} instead.
     */
    @Deprecated
    Transformer withFieldMapping(FieldMapping... fieldMapping);

    /**
     * Initializes the field transformer functions. The transformer function returns directly the field value.
     * @param fieldTransformer the fields transformer function
     * @return the {@link Transformer} instance
     * @deprecated use {@code withFieldMapping(com.hotels.transformer.model.FieldTransformer)} instead.
     */
    @Deprecated
    Transformer withFieldTransformer(FieldTransformer... fieldTransformer);
}
