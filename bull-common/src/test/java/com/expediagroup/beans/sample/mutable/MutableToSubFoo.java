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
package com.expediagroup.beans.sample.mutable;

import java.util.List;
import java.util.Map;

import com.expediagroup.beans.sample.ISubClass;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Sample mutable object.
 */
@Getter
@Setter
@ToString
public class MutableToSubFoo implements ISubClass {
    private String name;
    private int[] phoneNumbers;
    private Map<String, String> sampleMap;
    private Map<String, List<String>> complexMap;
    private Map<String, Map<String, String>> veryComplexMap;
}
