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
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.assertj.core.util.Lists.list;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;

import javax.annotation.processing.Generated;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.expediagroup.beans.generator.core.TransformerSpec;
import com.expediagroup.beans.generator.core.sample.javabean.Destination;
import com.expediagroup.beans.generator.core.sample.javabean.Source;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

/**
 * Tests for {@link SourceTransformerSpec}.
 */
public class SourceTransformerSpecTest {
    private final TypeSpec generated = TypeSpec.classBuilder("AtoBTransformer").build();

    @Spy
    private final Clock clock = Clock.fixed(Instant.EPOCH, ZoneId.systemDefault());

    @Mock
    private TransformerSpec spec;

    @InjectMocks
    private SourceTransformerSpec sourceTransformerSpec;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        when(spec.build(any(), any())).thenReturn(generated);
    }

    @AfterMethod
    public void tearDown() {
        sourceTransformerSpec = null;
    }

    @Test
    public void shouldDecorateATransformerSpec() {
        // WHEN
        TypeSpec sourceModel = sourceTransformerSpec.build(Source.class, Destination.class);

        // THEN
        assertSoftly(softly -> {
            softly.assertThat(sourceModel.name).as("Name is unchanged").isEqualTo(generated.name);
            softly.assertThat(sourceModel.javadoc.isEmpty()).as("Adds a Javadoc").isFalse();
            softly.assertThat(sourceModel.annotations).as("Adds a @Generated annotation")
                    .anyMatch(annotationSpec -> annotationSpec.type.equals(TypeName.get(Generated.class)));
        });
    }

    @Test
    public void shouldAnnotateGeneratedSourceWithTimestamp() {
        // WHEN
        TypeSpec sourceModel = sourceTransformerSpec.build(Source.class, Destination.class);

        // THEN
        var expectedTimeStamp = list(CodeBlock.of(clock.instant().toString()));
        assertThat(sourceModel.annotations)
                .anyMatch(annotationSpec -> annotationSpec.members.containsValue(expectedTimeStamp));
    }

    @Test
    public void shouldFailIfSpecIsNull() {
        // WHEN
        ThrowingCallable code = () -> new SourceTransformerSpec(null);

        // THEN
        assertThatThrownBy(code).isInstanceOf(NullPointerException.class);
    }

    @Test
    public void shouldFailIfClockIsNull() {
        // WHEN
        ThrowingCallable code = () -> new SourceTransformerSpec(spec, null);

        // THEN
        assertThatThrownBy(code).isInstanceOf(NullPointerException.class);
    }
}
