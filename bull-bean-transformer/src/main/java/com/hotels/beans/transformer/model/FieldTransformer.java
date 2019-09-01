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

package com.hotels.beans.transformer.model;

import java.util.function.Function;
import java.util.function.Supplier;

import lombok.Getter;
import lombok.ToString;

/**
 * Specifies the field mapping between the source object and destination one.
 *
 * @param <T> input field type.
 * @param <K> field value on with apply the function.
 * @deprecated This class will be removed since version 1.6.0 please use {@link com.hotels.transformer.model.FieldTransformer} instead.
 */
@Deprecated(since = "1.6.0", forRemoval = true)
@Getter
@ToString
public class FieldTransformer<T, K> extends com.hotels.transformer.model.FieldTransformer {
    /**
     * Creates a field transformer with a lambda function to be applied on the field.
     * @param destinationFieldName the field name in the destination object.
     * @param fieldTransformerFunction the transformer function to apply on field
     */
    public FieldTransformer(final String destinationFieldName, final Function<T, K> fieldTransformerFunction) {
        super(destinationFieldName, fieldTransformerFunction);
    }

    /**
     * Creates a field transformer with a field supplier function to be applied on the field.
     * @param destinationFieldName the field name in the destination object.
     * @param fieldTransformerSupplier the transformer supplier to apply on field
     */
    public FieldTransformer(final String destinationFieldName, final Supplier<K> fieldTransformerSupplier) {
        super(destinationFieldName, fieldTransformerSupplier);
    }
}
