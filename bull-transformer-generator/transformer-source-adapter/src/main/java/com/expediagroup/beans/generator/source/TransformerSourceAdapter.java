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
package com.expediagroup.beans.generator.source;

import java.nio.file.Path;
import java.nio.file.Paths;

import com.expediagroup.beans.generator.core.Transformer;
import com.expediagroup.beans.generator.core.TransformerSpec;

import lombok.Builder;

/**
 * A source adapter for {@link TransformerSpec} that creates a readable source file
 * representation from the model.
 * <br>
 * This component is for internal usage in Bull, it's intended as a low-level wrapper
 * for compile-time conversion of models. Clients should prefer higher level interfaces
 * such as {@code TransformerRegistry}.
 */
@Builder
public class TransformerSourceAdapter {
    /**
     * The default source path if none is specified.
     */
    static final Path DEFAULT_PATH = Paths.get("generated-sources/bull");

    /**
     * The default transformer package if none is specified.
     */
    static final String DEFAULT_PACKAGE = Transformer.class.getPackageName();

    /**
     * The path where to write the generated transformer sources.
     * The tree of package directories will start from this location.
     */
    @Builder.Default
    private final Path basePath = DEFAULT_PATH;

    /**
     * The Java package to use for the generated transformer sources.
     */
    @Builder.Default
    private final String packageName = DEFAULT_PACKAGE;

    /**
     * The Transformer model for generating source code.
     */
    private final TransformerSpec spec;

    /**
     * Create a new file containing the source of a {@link Transformer} from {@code source} to {@code destination}.
     * @param <A> the source type
     * @param <B> the destination type
     * @param source the source class
     * @param destination the destination class
     * @return a new Transformer source file
     */
    public <A, B> TransformerFile newTransformerFile(final Class<A> source, final Class<B> destination) {
        return new TransformerFile(
                new SourceTransformerSpec(spec).build(source, destination), basePath, packageName
        );
    }
}
