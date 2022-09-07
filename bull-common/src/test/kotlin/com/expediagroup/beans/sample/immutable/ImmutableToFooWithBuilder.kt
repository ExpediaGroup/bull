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
package com.expediagroup.beans.sample.immutable

import com.expediagroup.beans.sample.FromSubFoo
import java.math.BigInteger

/**
 * Immutable bean instantiable only through a Builder.
 */
class ImmutableToFooWithBuilder(
    val name: String?,
    val id: BigInteger?,
    val nestedObjectList: List<FromSubFoo>?,
    val list: List<String>?,
    val nestedObject: FromSubFoo?
) {

    class ImmutableToFooWithBuilderBuilder internal constructor() {
        private var name: String? = null
        private var id: BigInteger? = null
        private var nestedObjectList: List<FromSubFoo>? = null
        private var list: List<String>? = null
        private var nestedObject: FromSubFoo? = null

        /**
         * @return `this`.
         */
        fun name(name: String?): ImmutableToFooWithBuilderBuilder {
            this.name = name
            return this
        }

        /**
         * @return `this`.
         */
        fun id(id: BigInteger?): ImmutableToFooWithBuilderBuilder {
            this.id = id
            return this
        }

        /**
         * @return `this`.
         */
        fun nestedObjectList(nestedObjectList: List<FromSubFoo>?): ImmutableToFooWithBuilderBuilder {
            this.nestedObjectList = nestedObjectList
            return this
        }

        /**
         * @return `this`.
         */
        fun list(list: List<String>?): ImmutableToFooWithBuilderBuilder {
            this.list = list
            return this
        }

        /**
         * @return `this`.
         */
        fun nestedObject(nestedObject: FromSubFoo?): ImmutableToFooWithBuilderBuilder {
            this.nestedObject = nestedObject
            return this
        }

        fun build(): ImmutableToFooWithBuilder {
            return ImmutableToFooWithBuilder(name, id, nestedObjectList, list, nestedObject)
        }

        override fun toString(): String {
            return "ImmutableToFooWithBuilder.ImmutableToFooWithBuilderBuilder(name=$name, id=$id, nestedObjectList=$nestedObjectList, list=$list, nestedObject=$nestedObject)"
        }
    }

    companion object {
        fun builder(): ImmutableToFooWithBuilderBuilder {
            return ImmutableToFooWithBuilderBuilder()
        }
    }
}
