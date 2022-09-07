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
import com.expediagroup.transformer.annotation.ConstructorArg
import java.math.BigInteger

/**
 * Sample mixed object with different column names.
 */
class MixedToFooDiffFields(
    @param:ConstructorArg("name") var name: String,
    @param:ConstructorArg("identifier") val identifier: BigInteger,
    @param:ConstructorArg("list") val list: List<String>,
    @param:ConstructorArg("nestedObjectList") val nestedObjectList: List<ImmutableToSubFoo>,
    @param:ConstructorArg("nestedObject") var nestedObject: ImmutableToSubFoo
) {

    override fun toString(): String {
        return "MixedToFooDiffFields{" +
            "name='" + name + '\'' +
            ", identifier=" + identifier +
            ", list=" + list +
            ", nestedObjectList=" + nestedObjectList +
            ", nestedObject=" + nestedObject +
            '}'
    }
}
