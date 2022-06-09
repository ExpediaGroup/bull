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
package com.expediagroup.beans.sample;

import java.math.BigInteger;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Sample mixed object.
 */
@AllArgsConstructor
@Getter
@Setter
@ToString
public class FromFoo implements Cloneable {
    private final String name;
    private BigInteger id;
    private final List<FromSubFoo> nestedObjectList;
    private final List<String> list;
    private final FromSubFoo nestedObject;

    public FromFoo clone() throws CloneNotSupportedException {
        return (FromFoo) super.clone();
    }
}
