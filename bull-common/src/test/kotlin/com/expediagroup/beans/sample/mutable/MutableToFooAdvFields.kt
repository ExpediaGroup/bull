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

import com.expediagroup.beans.sample.ISubClass
import com.expediagroup.transformer.constant.ClassType
import java.util.*

/**
 * Sample mutable object containing special fields.
 */
class MutableToFooAdvFields {
    var name: Optional<String>? = null
    var age: Optional<Int>? = null
    private var indexNumber: String? = null
    private var classType: ClassType? = null
    private var locale: Locale? = null
    var list: List<*>? = null
    private var collection: Collection<Any>? = null
    var map: Map<*, *>? = null
    private var supertypeMap: Map<in Any, Any>? = null
    var nestedObject: ISubClass? = null
    private fun setIndex(indexNumber: String) {
        this.indexNumber = indexNumber
    }

    override fun toString(): String {
        return "MutableToFooAdvFields{" +
                "name=" + name +
                ", age=" + age +
                ", indexNumber='" + indexNumber + '\'' +
                ", classType=" + classType +
                ", locale=" + locale +
                ", list=" + list +
                ", collection=" + collection +
                ", map=" + map +
                ", supertypeMap=" + supertypeMap +
                ", nestedObject=" + nestedObject +
                '}'
    }
}