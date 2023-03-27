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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.google.common.jimfs.Configuration;
import com.google.common.jimfs.Jimfs;
import com.squareup.javapoet.TypeSpec;

/**
 * Tests for {@link TransformerFile}.
 */
public class TransformerFileTest {

    private static final String DEFAULT_PACKAGE = "";

    private final FileSystem fs = Jimfs.newFileSystem(Configuration.unix());

    private final TypeSpec generated = TypeSpec.classBuilder("Generated").build();

    private final String generatedFileName = generated.name + ".java";

    private Path outputDir;

    @BeforeClass
    public void setUp() throws IOException {
        outputDir = fs.getPath("/out");
        Files.createDirectory(outputDir);
    }

    @Test
    public void shouldWriteSourceInDefaultPackage() throws IOException {
        // GIVEN
        TransformerFile transformerFile = new TransformerFile(generated, outputDir, DEFAULT_PACKAGE);

        // WHEN
        Path generatedSource = transformerFile.write();

        // THEN
        assertThat(generatedSource).startsWith(outputDir)
                .hasFileName(generatedFileName)
                .hasContent(generated.toString());
    }

    @Test
    public void shouldWriteSourceInsidePackage() throws IOException {
        // GIVEN
        String packageName = "com.foo.bar";
        TransformerFile transformerFile = new TransformerFile(generated, outputDir, packageName);

        // WHEN
        Path generatedSource = transformerFile.write();

        // THEN
        String expectedContent = String.format("package %s;\n\n%s", packageName, generated);
        assertThat(generatedSource).startsWith(outputDir.resolve("com/foo/bar/"))
                .hasFileName(generatedFileName)
                .hasContent(expectedContent);
    }

    @Test
    public void shouldFailWriteIfDirectoryDoesNotExist() {
        // GIVEN
        Path nonExistent = fs.getPath("/non/existent");
        TransformerFile transformerFile = new TransformerFile(generated, nonExistent, DEFAULT_PACKAGE);

        // WHEN
        ThrowingCallable code = transformerFile::write;

        // THEN
        assertThatThrownBy(code).isInstanceOf(IOException.class);
    }
}
