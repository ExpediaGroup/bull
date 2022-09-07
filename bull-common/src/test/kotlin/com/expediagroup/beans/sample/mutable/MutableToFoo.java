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

import java.math.BigInteger;
import java.util.List;

/**
 * Sample mutable object.
 */
public class MutableToFoo {
    private String name;
    private BigInteger id;
    private List<String> list;
    private List<MutableToSubFoo> nestedObjectList;
    private MutableToSubFoo nestedObject;

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public BigInteger getId() {
        return id;
    }

    public void setId(final BigInteger id) {
        this.id = id;
    }

    public List<String> getList() {
        return list;
    }

    public void setList(final List<String> list) {
        this.list = list;
    }

    public List<MutableToSubFoo> getNestedObjectList() {
        return nestedObjectList;
    }

    public void setNestedObjectList(final List<MutableToSubFoo> nestedObjectList) {
        this.nestedObjectList = nestedObjectList;
    }

    public MutableToSubFoo getNestedObject() {
        return nestedObject;
    }

    public void setNestedObject(final MutableToSubFoo nestedObject) {
        this.nestedObject = nestedObject;
    }

    @Override
    public String toString() {
        return "MutableToFoo{" +
                "name='" + name + '\'' +
                ", id=" + id +
                ", list=" + list +
                ", nestedObjectList=" + nestedObjectList +
                ", nestedObject=" + nestedObject +
                '}';
    }
}
