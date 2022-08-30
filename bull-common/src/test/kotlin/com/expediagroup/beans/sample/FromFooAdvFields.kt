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
import lombok.AllArgsConstructor
import lombok.Getter
import lombok.Setter
import lombok.ToString
import java.util.*

/**
 * Sample object containing java advanced fields.
 */
@AllArgsConstructor
@Getter
@Setter
@ToString
class FromFooAdvFields : Cloneable {
    private val name: Optional<String>? = null
    private val age: Optional<Int>? = null
    private val indexNumber: String? = null
    private val classType: ClassType? = null
    private val locale: String? = null
    private val price = 0f
    private val list: List<*>? = null
    private val collection: Collection<in Any>? = null
    private val map: Map<*, *>? = null
    private val supertypeMap: Map<in Any, in Any>? = null
    private val nestedObject: ISubClass? = null

    @Throws(CloneNotSupportedException::class)
    public override fun clone(): FromFooAdvFields {
        return super.clone() as FromFooAdvFields
    }
}
