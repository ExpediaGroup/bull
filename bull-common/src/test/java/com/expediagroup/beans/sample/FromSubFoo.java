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

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Sample immutable object.
 */
public class FromSubFoo implements ISubClass {
    private final String name;
    private final int[] phoneNumbers;
    private final Map<String, String> sampleMap;
    private final Map<String, List<String>> complexMap;
    private final Map<String, Map<String, String>> veryComplexMap;

    public FromSubFoo(final String name, final int[] phoneNumbers, final Map<String, String> sampleMap, final Map<String, List<String>> complexMap, final Map<String, Map<String, String>> veryComplexMap) {
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
        return "FromSubFoo{" +
                "name='" + name + '\'' +
                ", phoneNumbers=" + Arrays.toString(phoneNumbers) +
                ", sampleMap=" + sampleMap +
                ", complexMap=" + complexMap +
                ", veryComplexMap=" + veryComplexMap +
                '}';
    }
}
