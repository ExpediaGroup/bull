/**
 * Copyright (C) 2019-2021 Expedia, Inc.
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
package com.hotels.beans.sample.immutable;

import java.util.List;
import java.util.Map;

import jakarta.validation.constraints.NotNull;

import com.hotels.transformer.annotation.ConstructorArg;

import lombok.Getter;
import lombok.ToString;

/**
 * Sample immutable object.
 */
@Getter
@ToString
public class ImmutableToSubFooCustomAnnotation {
    @NotNull
    private final String name;
    private final int[] phoneNumbers;
    private final Map<String, String> sampleMap;
    private final Map<String, List<String>> complexMap;
    private final Map<String, Map<String, String>> veryComplexMap;

    /**
     * Constructor.
     * @param name name
     */
    public ImmutableToSubFooCustomAnnotation(@ConstructorArg("name") final String name, @ConstructorArg("phoneNumbers") final int[] phoneNumbers,
        @ConstructorArg("sampleMap") final Map<String, String> sampleMap, @ConstructorArg("complexMap") final Map<String, List<String>> complexMap,
        @ConstructorArg("veryComplexMap") final Map<String, Map<String, String>> veryComplexMap) {
        this.name = name;
        this.phoneNumbers = phoneNumbers;
        this.sampleMap = sampleMap;
        this.complexMap = complexMap;
        this.veryComplexMap = veryComplexMap;
    }
}
