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
package com.expediagroup.beans.sample.mixed

import com.expediagroup.beans.sample.immutable.ImmutableToSubFoo
import java.math.BigInteger

/**
 * Mixed bean instantiable only through a Builder.
 */
class MixedToFooWithBuilder(
    val name: String?,
    val id: BigInteger?,
    val nestedObjectList: List<ImmutableToSubFoo>?,
    val list: List<String>?,
    val nestedObject: ImmutableToSubFoo?
) {

    class MixedToFooWithBuilderBuilder internal constructor() {
        private var name: String? = null
        private var id: BigInteger? = null
        private var nestedObjectList: List<ImmutableToSubFoo>? = null
        private var list: List<String>? = null
        private var nestedObject: ImmutableToSubFoo? = null

        /**
         * @return `this`.
         */
        fun name(name: String?): MixedToFooWithBuilderBuilder {
            this.name = name
            return this
        }

        /**
         * @return `this`.
         */
        fun id(id: BigInteger?): MixedToFooWithBuilderBuilder {
            this.id = id
            return this
        }

        /**
         * @return `this`.
         */
        fun nestedObjectList(nestedObjectList: List<ImmutableToSubFoo>?): MixedToFooWithBuilderBuilder {
            this.nestedObjectList = nestedObjectList
            return this
        }

        /**
         * @return `this`.
         */
        fun list(list: List<String>?): MixedToFooWithBuilderBuilder {
            this.list = list
            return this
        }

        /**
         * @return `this`.
         */
        fun nestedObject(nestedObject: ImmutableToSubFoo?): MixedToFooWithBuilderBuilder {
            this.nestedObject = nestedObject
            return this
        }

        fun build(): MixedToFooWithBuilder {
            return MixedToFooWithBuilder(name, id, nestedObjectList, list, nestedObject)
        }

        override fun toString(): String {
            return "MixedToFooWithBuilder.MixedToFooWithBuilderBuilder(name=$name, id=$id, nestedObjectList=$nestedObjectList, list=$list, nestedObject=$nestedObject)"
        }
    }

    companion object {
        fun builder(): MixedToFooWithBuilderBuilder {
            return MixedToFooWithBuilderBuilder()
        }
    }
}
