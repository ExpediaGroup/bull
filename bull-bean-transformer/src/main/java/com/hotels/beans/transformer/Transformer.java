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

import static java.util.Arrays.stream;
import static java.util.Objects.isNull;

import com.hotels.beans.transformer.model.FieldMapping;
import com.hotels.beans.transformer.model.FieldTransformer;

/**
 * Utility methods for all objects transformation.
 * @deprecated This class will be removed since version 1.6.0 please use {@link com.hotels.transformer.Transformer} instead.
 */
@Deprecated(since = "1.6.0", forRemoval = true)
public interface Transformer extends BeanTransformer {
    /**
     * Initializes the mapping between fields in the source object and the destination one.
     * @param fieldMapping the field mapping
     * @return the {@link Transformer} instance
     * @deprecated use {@code withFieldMapping(com.hotels.transformer.model.FieldMapping)} instead.
     */
    @Deprecated(since = "1.6.0", forRemoval = true)
    default Transformer withFieldMapping(FieldMapping... fieldMapping) {
        com.hotels.transformer.model.FieldMapping[] fieldMappings = stream(fieldMapping)
                .map(fm -> new com.hotels.transformer.model.FieldMapping(fm.getSourceFieldName(), fm.getDestFieldName()))
                .toArray(com.hotels.transformer.model.FieldMapping[]::new);
        return (Transformer) withFieldMapping(fieldMappings);
    }

    /**
     * Initializes the field transformer functions. The transformer function returns directly the field value.
     * @param fieldTransformer the fields transformer function
     * @return the {@link Transformer} instance
     * @deprecated use {@code withFieldMapping(com.hotels.transformer.model.FieldTransformer)} instead.
     */
    @Deprecated(since = "1.6.0", forRemoval = true)
    @SuppressWarnings("unchecked")
    default Transformer withFieldTransformer(FieldTransformer... fieldTransformer) {
        com.hotels.transformer.model.FieldTransformer[] fieldTransformers = stream(fieldTransformer)
                .map(tmpFt -> {
                    com.hotels.transformer.model.FieldTransformer ft;
                    if (isNull(tmpFt.getTransformerFunction())) {
                        ft = new com.hotels.transformer.model.FieldTransformer<>(tmpFt.getDestFieldName(), tmpFt.getTransformerSupplier());
                    } else {
                        ft = new com.hotels.transformer.model.FieldTransformer<>(tmpFt.getDestFieldName(), tmpFt.getTransformerFunction());
                    }
                    return ft;
                }).toArray(com.hotels.transformer.model.FieldTransformer[]::new);
        return (Transformer) withFieldTransformer(fieldTransformers);
    }
}
