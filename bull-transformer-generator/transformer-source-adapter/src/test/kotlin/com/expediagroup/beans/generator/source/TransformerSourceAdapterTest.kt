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
package com.expediagroup.beans.generator.source

import org.assertj.core.api.Assertions.assertThat

/**
 * Tests for [TransformerSourceAdapter].
 */
class TransformerSourceAdapterTest {
    private val generated: TypeSpec = TypeSpec.classBuilder("AtoBTransformer")
        .build()

    @Mock
    private val spec: TransformerSpec? = null

    @BeforeMethod
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        `when`(spec.build(any(), any())).thenReturn(generated)
    }

    @Test
    fun shouldCreateNewTransformerFile() {
        // GIVEN
        val basePath: Unit = Paths.get("./target")
        val packageName = "foo.bar"
        val transformerSourceAdapter: Unit = TransformerSourceAdapter.builder()
            .basePath(basePath)
            .packageName(packageName)
            .spec(spec)
            .build()

        // WHEN
        val result: TransformerFile =
            transformerSourceAdapter.newTransformerFile(Source::class.java, Destination::class.java)

        // THEN
        assertThat(result).extracting("basePath", "packageName")
            .containsExactly(basePath, packageName)
    }

    @Test
    fun shouldCreateNewTransformerFileWithDefaultValues() {
        // GIVEN
        val transformerSourceAdapter: Unit = TransformerSourceAdapter.builder()
            .spec(spec)
            .build()

        // WHEN
        val result: TransformerFile =
            transformerSourceAdapter.newTransformerFile(Source::class.java, Destination::class.java)

        // THEN
        assertThat(result).extracting("basePath", "packageName")
            .containsExactly(TransformerSourceAdapter.DEFAULT_PATH, TransformerSourceAdapter.DEFAULT_PACKAGE)
    }
}