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

import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import com.expediagroup.beans.sample.ISubClass;
import com.expediagroup.transformer.constant.ClassType;

/**
 * Sample mutable object containing special fields.
 */
public class MutableToFooAdvFields {
    private Optional<String> name;
    private Optional<Integer> age;
    private String indexNumber;
    private ClassType classType;
    private Locale locale;
    private List<?> list;
    private Collection<? super Object> collection;
    private Map<?, ?> map;
    private Map<? super Object, ? super Object> supertypeMap;
    private ISubClass nestedObject;

    public Optional<String> getName() {
        return name;
    }

    public void setName(final Optional<String> name) {
        this.name = name;
    }

    public Optional<Integer> getAge() {
        return age;
    }

    public void setAge(final Optional<Integer> age) {
        this.age = age;
    }

    public String getIndexNumber() {
        return indexNumber;
    }

    public void setIndexNumber(final String indexNumber) {
        this.indexNumber = indexNumber;
    }

    public ClassType getClassType() {
        return classType;
    }

    public void setClassType(final ClassType classType) {
        this.classType = classType;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(final Locale locale) {
        this.locale = locale;
    }

    public List<?> getList() {
        return list;
    }

    public void setList(final List<?> list) {
        this.list = list;
    }

    public Collection<? super Object> getCollection() {
        return collection;
    }

    public void setCollection(final Collection<? super Object> collection) {
        this.collection = collection;
    }

    public Map<?, ?> getMap() {
        return map;
    }

    public void setMap(final Map<?, ?> map) {
        this.map = map;
    }

    public Map<? super Object, ? super Object> getSupertypeMap() {
        return supertypeMap;
    }

    public void setSupertypeMap(final Map<? super Object, ? super Object> supertypeMap) {
        this.supertypeMap = supertypeMap;
    }

    public ISubClass getNestedObject() {
        return nestedObject;
    }

    public void setNestedObject(final ISubClass nestedObject) {
        this.nestedObject = nestedObject;
    }

    private void setIndex(final String indexNumber) {
        this.indexNumber = indexNumber;
    }

    @Override
    public String toString() {
        return "MutableToFooAdvFields{" +
                "name=" + name +
                ", age=" + age +
                ", indexNumber='" + indexNumber + '\'' +
                ", classType=" + classType +
                ", locale=" + locale +
                ", list=" + list +
                ", collection=" + collection +
                ", map=" + map +
                ", supertypeMap=" + supertypeMap +
                ", nestedObject=" + nestedObject +
                '}';
    }
}
