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
package com.expediagroup.beans.generator.core.mapping

import java.lang.String.format

/**
 * Tests for [JavaBeanCode].
 */
class JavaBeanCodeTest {
    /**
     * The class to be tested.
     */
    private val underTest: JavaBeanCode = JavaBeanCode(Source::class.java, Destination::class.java, ClassUtils())
    @Test
    fun shouldInstantiateAndReturnDestination() {
        // WHEN
        val block: CodeBlock = underTest.build()

        // THEN
        assertThat(block.toString())
            .contains(format("%1\$s destination = new %1\$s();", Destination::class.java.getName()))
            .contains("return destination;")
    }

    @Test
    fun shouldMapMatchingProperties() {
        // WHEN
        val block: CodeBlock = underTest.build()

        // THEN
        assertThat(block.toString())
            .contains("destination.setABoolean(source.isABoolean());")
            .contains("destination.setAString(source.getAString());")
    }
}