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
 * Sample mutable object.
 */
open class MutableToFoo {
    var name: String? = null
    var id: BigInteger? = null
    var list: List<String>? = null
    var nestedObjectList: List<MutableToSubFoo>? = null
    var nestedObject: MutableToSubFoo? = null
    override fun toString(): String {
        return "MutableToFoo{" +
                "name='" + name + '\'' +
                ", id=" + id +
                ", list=" + list +
                ", nestedObjectList=" + nestedObjectList +
                ", nestedObject=" + nestedObject +
                '}'
    }
}
