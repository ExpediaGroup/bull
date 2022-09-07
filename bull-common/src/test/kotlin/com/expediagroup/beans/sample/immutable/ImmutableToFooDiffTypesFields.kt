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

/**
 * Sample immutable object with different fields type than source object.
 */
class ImmutableToFooDiffTypesFields(
    val name: String,
    val id: String,
    val list: List<String>,
    val nestedObjectList: List<ImmutableToSubFoo>,
    val nestedObject: ImmutableToSubFoo
) {

    override fun toString(): String {
        return "ImmutableToFooDiffTypesFields{" +
                "name='" + name + '\'' +
                ", id='" + id + '\'' +
                ", list=" + list +
                ", nestedObjectList=" + nestedObjectList +
                ", nestedObject=" + nestedObject +
                '}'
    }
}