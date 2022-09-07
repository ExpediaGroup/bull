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

import java.math.BigInteger

/**
 * Mutable object instantiable only through a Builder.
 */
class MutableToFooWithBuilder private constructor(builder: Builder) {
    var name: String?
    var id: BigInteger?
    var list: List<String>?
    var nestedObjectList: List<MutableToSubFoo>?
    var nestedObject: MutableToSubFoo?

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

        fun build(): MutableToFooWithBuilder {
            return MutableToFooWithBuilder(this)
        }
    }
}
