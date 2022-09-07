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
package com.expediagroup.beans.sample.immutable;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotNull;

import com.expediagroup.transformer.annotation.ConstructorArg;

/**
 * Sample immutable object.
 */
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

    public String getName() {
        return name;
    }

    public int[] getPhoneNumbers() {
        return phoneNumbers;
    }

    public Map<String, String> getSampleMap() {
        return sampleMap;
    }

    public Map<String, List<String>> getComplexMap() {
        return complexMap;
    }

    public Map<String, Map<String, String>> getVeryComplexMap() {
        return veryComplexMap;
    }

    @Override
    public String toString() {
        return "ImmutableToSubFooCustomAnnotation{" +
                "name='" + name + '\'' +
                ", phoneNumbers=" + Arrays.toString(phoneNumbers) +
                ", sampleMap=" + sampleMap +
                ", complexMap=" + complexMap +
                ", veryComplexMap=" + veryComplexMap +
                '}';
    }
}
