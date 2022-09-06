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

import java.util.List;
import java.util.Map;

/**
 * Sample immutable object containing extremely complex map.
 */
public class FromFooMap {
    private final Map<String, String> sampleMap;
    private final Map<String, List<String>> complexMap;
    private final Map<String, Map<String, String>> veryComplexMap;
    private final Map<FromFooSimple, Map<String, String>> extremeComplexMap;
    private final Map unparametrizedMap;

    public FromFooMap(final Map<String, String> sampleMap, final Map<String, List<String>> complexMap, final Map<String, Map<String, String>> veryComplexMap, final Map<FromFooSimple, Map<String, String>> extremeComplexMap, final Map unparametrizedMap) {
        this.sampleMap = sampleMap;
        this.complexMap = complexMap;
        this.veryComplexMap = veryComplexMap;
        this.extremeComplexMap = extremeComplexMap;
        this.unparametrizedMap = unparametrizedMap;
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

    public Map<FromFooSimple, Map<String, String>> getExtremeComplexMap() {
        return extremeComplexMap;
    }

    public Map getUnparametrizedMap() {
        return unparametrizedMap;
    }

    @Override
    public String toString() {
        return "FromFooMap{" +
                "sampleMap=" + sampleMap +
                ", complexMap=" + complexMap +
                ", veryComplexMap=" + veryComplexMap +
                ", extremeComplexMap=" + extremeComplexMap +
                ", unparametrizedMap=" + unparametrizedMap +
                '}';
    }
}
