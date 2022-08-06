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
package com.expediagroup.beans.generator.core;

/**
 * A {@code Transformer} can convert an object of a source type to a destination type.
 * @param <A> the source type
 * @param <B> the destination type
 */
public interface Transformer<A, B> {
    /**
     * Transform an instance of {@code A} into a new instance of {@code B}.
     * The destination instance is initialized with values copied from the source.
     * Values can be optionally transformed depending on the implementation.
     * @param source the source object to convert
     * @return a new instance of {@code B} initialized
     */
    B transform(A source);
}
