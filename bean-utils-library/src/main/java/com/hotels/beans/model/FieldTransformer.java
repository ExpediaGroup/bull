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

package com.hotels.beans.model;

import static java.util.Objects.nonNull;

import java.util.function.Function;
import java.util.function.Supplier;

import lombok.Getter;
import lombok.ToString;

/**
 * Specifies the field mapping between the source object and destination one.
 *
 * @param <T> input field type.
 * @param <K> field value on with apply the function.
 */
@Getter
@ToString
public class FieldTransformer<T, K> {
    /**
     * The field name in the destination object.
     */
    private final String destFieldName;

    /**
     * The field transformer function.
     */
    private Function<T, K> transformerFunction;

    /**
     * The field transformer supplier.
     */
    private Supplier<K> transformerSupplier;

    /**
     * Creates a field transformer with a lambda function to be applied on the field.
     * @param destFieldName the field name in the destination object.
     * @param transformerFunction the transformer function to apply on field
     */
    public FieldTransformer(final String destFieldName, final Function<T, K> transformerFunction) {
        this.destFieldName = destFieldName;
        this.transformerFunction = transformerFunction;
    }

    /**
     * Creates a field transformer with a field supplier function to be applied on the field.
     * @param destFieldName the field name in the destination object.
     * @param transformerSupplier the transformer supplier to apply on field
     */
    public FieldTransformer(final String destFieldName, final Supplier<K> transformerSupplier) {
        this.destFieldName = destFieldName;
        this.transformerSupplier = transformerSupplier;
    }

    /**
     * Returns a transformed object by applying the defined transformed function or the supplier.
     * @param objectToTransform the object to transform
     * @return the transformed object
     */
    public final K getTransformedObject(final T objectToTransform) {
        return nonNull(transformerFunction) ? transformerFunction.apply(objectToTransform) : transformerSupplier.get();
    }
}
