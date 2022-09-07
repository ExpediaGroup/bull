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

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.expediagroup.transformer.constant.ClassType;

/**
 * Sample object containing java advanced fields.
 */
public class FromFooAdvFields implements Cloneable {
    private final Optional<Integer> age;
    private final String indexNumber;
    private final ClassType classType;
    private final String locale;
    private final float price;
    private final List<?> list;
    private final Collection<? super Object> collection;
    private final Map<?, ?> map;
    private final Map<? super Object, ? super Object> supertypeMap;
    private final ISubClass nestedObject;
    private Optional<String> name;

    public FromFooAdvFields(final Optional<String> name, final Optional<Integer> age, final String indexNumber, final ClassType classType, final String locale, final float price, final List<?> list, final Collection<? super Object> collection, final Map<?, ?> map, final Map<? super Object, ? super Object> supertypeMap, final ISubClass nestedObject) {
        this.name = name;
        this.age = age;
        this.indexNumber = indexNumber;
        this.classType = classType;
        this.locale = locale;
        this.price = price;
        this.list = list;
        this.collection = collection;
        this.map = map;
        this.supertypeMap = supertypeMap;
        this.nestedObject = nestedObject;
    }

    public FromFooAdvFields clone() throws CloneNotSupportedException {
        return (FromFooAdvFields) super.clone();
    }

    public Optional<String> getName() {
        return name;
    }

    public void setName(final Optional<String> name) {
        this.name = name;
    }

    public Optional<Integer> getAge() {
        return age;
    }

    public String getIndexNumber() {
        return indexNumber;
    }

    public ClassType getClassType() {
        return classType;
    }

    public String getLocale() {
        return locale;
    }

    public float getPrice() {
        return price;
    }

    public List<?> getList() {
        return list;
    }

    public Collection<? super Object> getCollection() {
        return collection;
    }

    public Map<?, ?> getMap() {
        return map;
    }

    public Map<? super Object, ? super Object> getSupertypeMap() {
        return supertypeMap;
    }

    public ISubClass getNestedObject() {
        return nestedObject;
    }

    @Override
    public String toString() {
        return "FromFooAdvFields{" +
                "name=" + name +
                ", age=" + age +
                ", indexNumber='" + indexNumber + '\'' +
                ", classType=" + classType +
                ", locale='" + locale + '\'' +
                ", price=" + price +
                ", list=" + list +
                ", collection=" + collection +
                ", map=" + map +
                ", supertypeMap=" + supertypeMap +
                ", nestedObject=" + nestedObject +
                '}';
    }
}
