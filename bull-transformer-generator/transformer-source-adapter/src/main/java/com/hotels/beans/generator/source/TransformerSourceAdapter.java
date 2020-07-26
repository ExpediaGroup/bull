/**
 * Copyright (C) 2019-2020 Expedia, Inc.
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

package com.hotels.beans.generator.source;

import java.nio.file.Path;
import java.nio.file.Paths;

import com.hotels.beans.generator.core.Transformer;

import lombok.Builder;

/**
 * TransformerSourceAdapter. TODO: update this documentation.
 */
@Builder
public class TransformerSourceAdapter {
    /**
     * The default source path if none is specified.
     */
    private static final Path DEFAULT_PATH = Paths.get("generated-sources/bull");

    /**
     * The default transformer package if none is specified.
     */
    private static final String DEFAULT_PACKAGE = Transformer.class.getPackageName();

    @Builder.Default
    private final Path basePath = DEFAULT_PATH;

    @Builder.Default
    private final String packageName = DEFAULT_PACKAGE;

    private final SourceTransformerSpec spec;

    public <A, B> TransformerFile newTransformerFile(final Class<A> source, final Class<B> destination) {
        return new TransformerFile(spec.build(source, destination), basePath, packageName);
    }
}
