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

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.expediagroup.beans.sample.ISubClass;

/**
 * Sample mutable object.
 */
public class MutableToSubFoo implements ISubClass {
    private String name;
    private int[] phoneNumbers;
    private Map<String, String> sampleMap;
    private Map<String, List<String>> complexMap;
    private Map<String, Map<String, String>> veryComplexMap;

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public int[] getPhoneNumbers() {
        return phoneNumbers;
    }

    public void setPhoneNumbers(final int[] phoneNumbers) {
        this.phoneNumbers = phoneNumbers;
    }

    public Map<String, String> getSampleMap() {
        return sampleMap;
    }

    public void setSampleMap(final Map<String, String> sampleMap) {
        this.sampleMap = sampleMap;
    }

    public Map<String, List<String>> getComplexMap() {
        return complexMap;
    }

    public void setComplexMap(final Map<String, List<String>> complexMap) {
        this.complexMap = complexMap;
    }

    public Map<String, Map<String, String>> getVeryComplexMap() {
        return veryComplexMap;
    }

    public void setVeryComplexMap(final Map<String, Map<String, String>> veryComplexMap) {
        this.veryComplexMap = veryComplexMap;
    }

    @Override
    public String toString() {
        return "MutableToSubFoo{" +
                "name='" + name + '\'' +
                ", phoneNumbers=" + Arrays.toString(phoneNumbers) +
                ", sampleMap=" + sampleMap +
                ", complexMap=" + complexMap +
                ", veryComplexMap=" + veryComplexMap +
                '}';
    }
}
