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
 * Tests for [SourceTransformerSpec].
 */
class SourceTransformerSpecTest {
    private val generated: TypeSpec = TypeSpec.classBuilder("AtoBTransformer")
        .build()

    @Spy
    private val clock: Clock = Clock.fixed(Instant.EPOCH, ZoneId.systemDefault())

    @Mock
    private lateinit var spec: TransformerSpec

    @InjectMocks
    private lateinit var sourceTransformerSpec: SourceTransformerSpec

    @BeforeMethod
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        `when`(spec.build(any(), any())).thenReturn(generated)
    }

    @AfterMethod
    fun tearDown() {
        sourceTransformerSpec = null
    }

    @Test
    fun shouldDecorateATransformerSpec() {
        // WHEN
        val sourceModel: TypeSpec = sourceTransformerSpec.build(Source::class.java, Destination::class.java)

        // THEN
        assertSoftly { softly ->
            softly.assertThat(sourceModel.name)
                .`as`("Name is unchanged")
                .isEqualTo(generated.name)
            softly.assertThat(sourceModel.javadoc.isEmpty())
                .`as`("Adds a Javadoc")
                .isFalse()
            softly.assertThat(sourceModel.annotations)
                .`as`("Adds a @Generated annotation")
                .anyMatch { annotationSpec -> annotationSpec.type.equals(TypeName.get(Generated::class.java)) }
        }
    }

    @Test
    fun shouldAnnotateGeneratedSourceWithTimestamp() {
        // WHEN
        val sourceModel: TypeSpec = sourceTransformerSpec.build(Source::class.java, Destination::class.java)

        // THEN
        val expectedTimeStamp: Unit = list(
            CodeBlock.of(
                clock.instant()
                    .toString()
            )
        )
        assertThat(sourceModel.annotations)
            .anyMatch { annotationSpec -> annotationSpec.members.containsValue(expectedTimeStamp) }
    }

    @Test
    fun shouldFailIfSpecIsNull() {
        // WHEN
        val code = ThrowingCallable { SourceTransformerSpec(null) }

        // THEN
        assertThatThrownBy(code).isInstanceOf(NullPointerException::class.java)
    }

    @Test
    fun shouldFailIfClockIsNull() {
        // WHEN
        val code = ThrowingCallable { SourceTransformerSpec(spec, null) }

        // THEN
        assertThatThrownBy(code).isInstanceOf(NullPointerException::class.java)
    }
}
