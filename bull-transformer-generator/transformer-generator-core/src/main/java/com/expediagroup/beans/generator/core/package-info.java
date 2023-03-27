/**
 * Copyright (C) 2018-2023 Expedia, Inc.
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
/**
 * Contains the main transformer model constructed as a {@link com.squareup.javapoet.TypeSpec} object.
 * A model can then be further modified and eventually converted to source code or byte code.
 * The main class to obtain a transformer model is {@link com.expediagroup.beans.generator.core.TransformerSpec},
 * which can create transformers from type {@code A} to type {@code B}.
 * <p>
 * All transformer models implement the {@link com.expediagroup.beans.generator.core.Transformer} interface.
 * The mapping code model is created using {@link com.expediagroup.beans.generator.core.mapping.MappingCode}.
 */
package com.expediagroup.beans.generator.core;
