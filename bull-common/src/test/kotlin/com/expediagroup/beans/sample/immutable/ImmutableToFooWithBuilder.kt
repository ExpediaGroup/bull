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

import com.expediagroup.beans.sample.FromSubFoo
import lombok.Builder
import lombok.Getter
import java.math.BigInteger

/**
 * Immutable bean instantiable only through a Builder.
 */
@Getter
@Builder
class ImmutableToFooWithBuilder(
    private val name: String,
    private val id: BigInteger,
    private val nestedObjectList: List<FromSubFoo>,
    private val list: List<String>,
    private val nestedObject: FromSubFoo
)
