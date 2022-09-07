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

import java.math.BigInteger
import java.util.*

/**
 * Sample immutable object.
 */
class ImmutableToFooSimple(val name: String?, val id: BigInteger?, private val isActive: Boolean?) {

    override fun toString(): String {
        return "ImmutableToFooSimple{" +
            "name='" + name + '\'' +
            ", id=" + id +
            ", active=" + isActive +
            '}'
    }

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val that = o as ImmutableToFooSimple
        return isActive == that.isActive && name == that.name && id == that.id
    }

    override fun hashCode(): Int {
        return Objects.hash(name, id, isActive)
    }
}
