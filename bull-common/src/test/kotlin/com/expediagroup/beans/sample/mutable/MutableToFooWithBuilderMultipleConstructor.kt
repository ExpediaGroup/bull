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
 * The Builder class contains multiple constructors.
 */
class MutableToFooWithBuilderMultipleConstructor
/**
 * Private constructor.
 */
private constructor() {
    var name: String? = null
        private set
    var id: BigInteger? = null
        private set
    var list: List<String>? = null
        private set
    var nestedObjectList: List<MutableToSubFoo>? = null
        private set
    var nestedObject: MutableToSubFoo? = null
        private set

    /**
     * Builder class.
     */
    internal class Builder {
        private var name: String? = null
        private var id: BigInteger? = null
        private val list: List<String>? = null
        private val nestedObjectList: List<MutableToSubFoo>? = null
        private val nestedObject: MutableToSubFoo? = null

        constructor(name: String?) {
            this.name = name
        }

        constructor() {}

        fun withName(name: String?): Builder {
            this.name = name
            return this
        }

        fun withId(id: BigInteger?): Builder {
            this.id = id
            return this
        }

        fun build(): MutableToFooWithBuilderMultipleConstructor {
            val builderToFoo = MutableToFooWithBuilderMultipleConstructor()
            builderToFoo.id = id
            builderToFoo.name = name
            builderToFoo.list = list
            builderToFoo.nestedObjectList = nestedObjectList
            builderToFoo.nestedObject = nestedObject
            return builderToFoo
        }
    }
}
