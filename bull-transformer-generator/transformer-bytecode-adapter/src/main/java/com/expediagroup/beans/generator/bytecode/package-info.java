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
/**
 * Contains the bytecode adapter classes, which can compile the source code
 * represented by a {@link com.expediagroup.beans.generator.core.TransformerSpec} at runtime,
 * and create a new instance of the generated {@link com.expediagroup.beans.generator.core.Transformer}.
 * <p>
 * This module is intended for internal usage in Bull as it's a low-level wrapper for
 * runtime compilation of models. Use it directly only if you need to customize the runtime
 * compilation and loading of models.
 * Clients should prefer higher level interfaces such as {@code TransformerRegistry}
 * to obtain generated transformer instances.
 */
package com.expediagroup.beans.generator.bytecode;
