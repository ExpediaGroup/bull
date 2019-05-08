/**
 * Copyright (C) 2019 Expedia, Inc.
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

package com.hotels.beans.sample;

import java.util.Optional;

import com.hotels.beans.constant.ClassType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Sample object containing java advanced fields.
 */
@AllArgsConstructor
@Getter
@Setter
@ToString
public class FromFooAdvFields implements Cloneable {
    private Optional<String> name;
    private final Optional<Integer> age;
    private final String indexNumber;
    private final ClassType classType;
    private final String locale;

    public FromFooAdvFields clone() throws CloneNotSupportedException {
        return (FromFooAdvFields) super.clone();
    }
}
