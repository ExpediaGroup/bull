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

import static lombok.AccessLevel.PROTECTED;

import java.io.IOException;
import java.nio.file.Path;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;

import lombok.AllArgsConstructor;

/**
 * A {@link com.expediagroup.beans.generator.core.Transformer} implementation source file.
 * This file can be written on disk using {@link #write()}
 * and contains all the data needed to perform this operation.
 */
@AllArgsConstructor(access = PROTECTED)
public final class TransformerFile {
    /**
     * The transformer source model.
     */
    private final TypeSpec typeSpec;

    /**
     * The path where to write the file and package directories.
     */
    private final Path basePath;

    /**
     * The Java package of this source file.
     */
    private final String packageName;

    /**
     * Write this source file and package directories to disk.
     * Returns the path where the source is actually written.
     * @return the path where this source is written
     * @throws IOException if a write error occurs
     */
    public Path write() throws IOException {
        return JavaFile.builder(packageName, typeSpec)
                .skipJavaLangImports(true)
                .build()
                .writeToPath(basePath);
    }
}
