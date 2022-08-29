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

import lombok.AllArgsConstructor
import lombok.Getter
import lombok.ToString
import java.math.BigInteger

/**
 * Sample immutable object containing properties available in the source object inside other objects.
 *
 *
 * e.g. phoneNumbers field is available inside `FromFoo.nestedObject.phoneNumbers`
 *
 */
@AllArgsConstructor
@Getter
@ToString
class ImmutableFlatToFoo {
    private val name: String? = null
    private val id: BigInteger? = null
    private val phoneNumbers: IntArray
}