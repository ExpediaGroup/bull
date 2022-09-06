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

/**
 * Sample immutable object.
 */
public class FromFooWithPrimitiveFields {
    private final String name;
    private final int id;
    private final int age;
    private final List<FromSubFoo> nestedObjectList;
    private final List<String> list;
    private final FromSubFoo nestedObject;

    public FromFooWithPrimitiveFields(final String name, final int id, final int age, final List<FromSubFoo> nestedObjectList, final List<String> list, final FromSubFoo nestedObject) {
        this.name = name;
        this.id = id;
        this.age = age;
        this.nestedObjectList = nestedObjectList;
        this.list = list;
        this.nestedObject = nestedObject;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public int getAge() {
        return age;
    }

    public List<FromSubFoo> getNestedObjectList() {
        return nestedObjectList;
    }

    public List<String> getList() {
        return list;
    }

    public FromSubFoo getNestedObject() {
        return nestedObject;
    }

    @Override
    public String toString() {
        return "FromFooWithPrimitiveFields{" +
                "name='" + name + '\'' +
                ", id=" + id +
                ", age=" + age +
                ", nestedObjectList=" + nestedObjectList +
                ", list=" + list +
                ", nestedObject=" + nestedObject +
                '}';
    }
}
