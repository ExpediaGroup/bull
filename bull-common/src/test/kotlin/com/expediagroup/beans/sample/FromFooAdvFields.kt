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
package com.expediagroup.beans.sample

import com.expediagroup.transformer.constant.ClassType
import java.util.*

/**
 * Sample object containing java advanced fields.
 */
class FromFooAdvFields(
    var name: Optional<String>,
    val age: Optional<Int>,
    private val indexNumber: String?,
    private val classType: ClassType,
    val locale: String,
    val price: Float,
    val list: List<*>?,
    collection: Collection<Any>?,
    map: Map<*, *>,
    supertypeMap: Map<in Any, Any>,
    nestedObject: ISubClass?
) : Cloneable {
    private val collection: Collection<Any>?
    val map: Map<*, *>
    private val supertypeMap: Map<in Any, Any>
    val nestedObject: ISubClass?

    init {
        this.collection = collection
        this.map = map
        this.supertypeMap = supertypeMap
        this.nestedObject = nestedObject
    }

    @Throws(CloneNotSupportedException::class)
    public override fun clone(): FromFooAdvFields {
        return super.clone() as FromFooAdvFields
    }

    override fun toString(): String {
        return "FromFooAdvFields{" +
            "name=" + name +
            ", age=" + age +
            ", indexNumber='" + indexNumber + '\'' +
            ", classType=" + classType +
            ", locale='" + locale + '\'' +
            ", price=" + price +
            ", list=" + list +
            ", collection=" + collection +
            ", map=" + map +
            ", supertypeMap=" + supertypeMap +
            ", nestedObject=" + nestedObject +
            '}'
    }
}
