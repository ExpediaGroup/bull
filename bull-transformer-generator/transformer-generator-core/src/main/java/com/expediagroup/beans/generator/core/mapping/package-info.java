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
 * Provides the models of code blocks that map a source type to a destination type,
 * specialized for different kind of classes, like: mutable, immutable and mixed.
 * The main entry point to obtain a mapping model is the factory method
 * {@link com.expediagroup.beans.generator.core.mapping.MappingCode#of(java.lang.Class, java.lang.Class)}.
 */
package com.expediagroup.beans.generator.core.mapping;
