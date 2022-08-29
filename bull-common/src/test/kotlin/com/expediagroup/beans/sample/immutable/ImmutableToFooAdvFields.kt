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

import com.expediagroup.beans.sample.ISubClass
import com.expediagroup.transformer.constant.ClassType
import lombok.AllArgsConstructor
import lombok.Getter
import lombok.ToString
import java.util.*

/**
 * Sample immutable object.
 */
@AllArgsConstructor
@Getter
@ToString
class ImmutableToFooAdvFields {
    private val name: Optional<String>? = null
    private val age: Int? = null
    private val indexNumber: String? = null
    private val classType: ClassType? = null
    private val locale: Locale? = null
    private val price: Price? = null
    private val list: List<*>? = null
    private val collection: Collection<in Any>? = null
    private val map: Map<*, *>? = null
    private val supertypeMap: Map<in Any, in Any>? = null
    private val nestedObject: ISubClass? = null
}

/**
 * Nested class.
 */
@AllArgsConstructor
@Getter
internal class Price {
    private val netPrice = 0f
    private val grossPrice = 0f
}