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
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

import java.nio.file.Paths;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.expediagroup.beans.generator.core.TransformerSpec;
import com.expediagroup.beans.generator.core.sample.javabean.Destination;
import com.expediagroup.beans.generator.core.sample.javabean.Source;
import com.squareup.javapoet.TypeSpec;

/**
 * Tests for {@link TransformerSourceAdapter}.
 */
public class TransformerSourceAdapterTest {
    private final TypeSpec generated = TypeSpec.classBuilder("AtoBTransformer").build();

    @Mock
    private TransformerSpec spec;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        when(spec.build(any(), any())).thenReturn(generated);
    }

    @Test
    public void shouldCreateNewTransformerFile() {
        // GIVEN
        var basePath = Paths.get("./target");
        var packageName = "foo.bar";
        var transformerSourceAdapter = TransformerSourceAdapter.builder()
                .basePath(basePath)
                .packageName(packageName)
                .spec(spec)
                .build();

        // WHEN
        TransformerFile result = transformerSourceAdapter.newTransformerFile(Source.class, Destination.class);

        // THEN
        assertThat(result).extracting("basePath", "packageName")
                .containsExactly(basePath, packageName);
    }

    @Test
    public void shouldCreateNewTransformerFileWithDefaultValues() {
        // GIVEN
        var transformerSourceAdapter = TransformerSourceAdapter.builder()
                .spec(spec)
                .build();

        // WHEN
        TransformerFile result = transformerSourceAdapter.newTransformerFile(Source.class, Destination.class);

        // THEN
        assertThat(result).extracting("basePath", "packageName")
                .containsExactly(TransformerSourceAdapter.DEFAULT_PATH, TransformerSourceAdapter.DEFAULT_PACKAGE);
    }
}
