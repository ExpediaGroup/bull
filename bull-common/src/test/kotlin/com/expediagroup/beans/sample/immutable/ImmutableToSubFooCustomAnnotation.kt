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

import com.expediagroup.transformer.annotation.ConstructorArg
import javax.validation.constraints.NotNull

/**
 * Sample immutable object.
 */
class ImmutableToSubFooCustomAnnotation
/**
 * Constructor.
 * @param name name
 */(
    @param:ConstructorArg("name") val name: @NotNull String?,
    @param:ConstructorArg(
        "phoneNumbers"
    ) val phoneNumbers: IntArray,
    @param:ConstructorArg("sampleMap") val sampleMap: Map<String, String>,
    @param:ConstructorArg(
        "complexMap"
    ) val complexMap: Map<String, List<String>>,
    @param:ConstructorArg("veryComplexMap") val veryComplexMap: Map<String, Map<String, String>>
) {

    override fun toString(): String {
        return "ImmutableToSubFooCustomAnnotation{" +
            "name='" + name + '\'' +
            ", phoneNumbers=" + phoneNumbers.contentToString() +
            ", sampleMap=" + sampleMap +
            ", complexMap=" + complexMap +
            ", veryComplexMap=" + veryComplexMap +
            '}'
    }
}
