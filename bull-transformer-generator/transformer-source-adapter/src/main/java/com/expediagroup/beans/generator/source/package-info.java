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
 * Contains the source adapter classes, which take the transformers generated from the Core module
 * and decorate them to produce readable source file representations.
 * <p>
 * The main class is {@link com.expediagroup.beans.generator.source.TransformerSourceAdapter} that
 * converts the source code represented by {@link com.expediagroup.beans.generator.core.TransformerSpec}
 * into a {@link com.expediagroup.beans.generator.source.TransformerFile}, which encapsulates all the information
 * to write that file on disk at a later time.
 * <p>
 * This component is for internal usage in Bull, as it's intended as a low-level wrapper
 * for compile-time conversion of models. Use it directly only if you need to customize the compile-time
 * transformation and writing of models or, for example, if you need to implement a command-line utility.
 * Clients should prefer higher level interfaces such as {@code TransformerRegistry}.
 */
package com.expediagroup.beans.generator.source;
