/**
 * Copyright (C) 2019-2022 Expedia, Inc.
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
package com.expediagroup.beans.sample.mutable

import lombok.Getter
import lombok.Setter
import java.math.BigInteger

/**
 * Mutable object instantiable only through a wrong Builder.
 */
@Getter
@Setter
class MutableToFooWithWrongBuilder private constructor(builder: Builder) {
    private val name: String?
    private val id: BigInteger?
    private val list: List<String>?
    private val nestedObjectList: List<MutableToSubFoo>?
    private val nestedObject: MutableToSubFoo?

    /**
     * Private constructor.
     * @param builder the builder class
     */
    init {
        id = builder.id
        name = builder.name
        list = builder.list
        nestedObjectList = builder.nestedObjectList
        nestedObject = builder.nestedObject
    }

    /**
     * Builder class.
     */
    class Builder {
        var name: String? = null
        var id: BigInteger? = null
        val list: List<String>? = null
        val nestedObjectList: List<MutableToSubFoo>? = null
        val nestedObject: MutableToSubFoo? = null
        fun withName(name: String?): Builder {
            this.name = name
            return this
        }

        fun withId(id: BigInteger?): Builder {
            this.id = id
            return this
        }

        fun build(): Builder {
            return this
        }
    }
}