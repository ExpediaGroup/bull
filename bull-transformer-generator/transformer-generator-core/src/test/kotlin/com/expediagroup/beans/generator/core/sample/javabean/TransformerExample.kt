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
package com.expediagroup.beans.generator.core.sample.javabean

import com.expediagroup.beans.generator.core.Transformer

/**
 * Example used as reference for the generated code.
 */
@SuppressWarnings("unused")
class TransformerExample : Transformer<Source?, Destination?> {
    @Override
    fun transform(source: Source): Destination { // 'final' required by Checkstyle, not part of the generated code
        val destination = Destination()
        destination.setABoolean(source.isABoolean())
        destination.setAString(source.getAString())
        return destination
    }

    companion object {
        /**
         * Entry point for manual testing.
         * Run this to compare transformer serialization with the above reference code.
         * @param args the args
         */
        fun main(args: Array<String?>?) {
            System.out.println(
                TransformerSpec(MappingCodeFactory.newInstance())
                    .build(Source::class.java, Destination::class.java)
            )
        }
    }
}
