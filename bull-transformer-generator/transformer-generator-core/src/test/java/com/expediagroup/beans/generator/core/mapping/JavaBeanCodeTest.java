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
package com.expediagroup.beans.generator.core.mapping;

import static java.lang.String.format;

import static org.assertj.core.api.Assertions.assertThat;

import org.testng.annotations.Test;

import com.expediagroup.beans.generator.core.sample.javabean.Destination;
import com.expediagroup.beans.generator.core.sample.javabean.Source;
import com.expediagroup.transformer.utils.ClassUtils;
import com.squareup.javapoet.CodeBlock;

/**
 * Tests for {@link JavaBeanCode}.
 */
public class JavaBeanCodeTest {
    /**
     * The class to be tested.
     */
    private final JavaBeanCode underTest = new JavaBeanCode(Source.class, Destination.class, new ClassUtils());

    @Test
    public void shouldInstantiateAndReturnDestination() {
        // WHEN
        CodeBlock block = underTest.build();

        // THEN
        assertThat(block.toString())
                .contains(format("%1$s destination = new %1$s();", Destination.class.getName()))
                .contains("return destination;");
    }

    @Test
    public void shouldMapMatchingProperties() {
        // WHEN
        CodeBlock block = underTest.build();

        // THEN
        assertThat(block.toString())
                .contains("destination.setABoolean(source.isABoolean());")
                .contains("destination.setAString(source.getAString());");
    }
}
