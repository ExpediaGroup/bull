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

import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import com.expediagroup.beans.sample.ISubClass;
import com.expediagroup.transformer.constant.ClassType;

/**
 * Sample immutable object.
 */
public class ImmutableToFooAdvFields {
    private final Optional<String> name;
    private final Integer age;
    private final String indexNumber;
    private final ClassType classType;
    private final Locale locale;
    private final Price price;
    private final List<?> list;
    private final Collection<? super Object> collection;
    private final Map<?, ?> map;
    private final Map<? super Object, ? super Object> supertypeMap;
    private final ISubClass nestedObject;

    public ImmutableToFooAdvFields(final Optional<String> name, final Integer age, final String indexNumber, final ClassType classType, final Locale locale, final Price price, final List<?> list, final Collection<? super Object> collection, final Map<?, ?> map, final Map<? super Object, ? super Object> supertypeMap, final ISubClass nestedObject) {
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

    public Optional<String> getName() {
        return name;
    }

    public Integer getAge() {
        return age;
    }

    public String getIndexNumber() {
        return indexNumber;
    }

    public ClassType getClassType() {
        return classType;
    }

    public Locale getLocale() {
        return locale;
    }

    public Price getPrice() {
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
        return "ImmutableToFooAdvFields{" +
                "name=" + name +
                ", age=" + age +
                ", indexNumber='" + indexNumber + '\'' +
                ", classType=" + classType +
                ", locale=" + locale +
                ", price=" + price +
                ", list=" + list +
                ", collection=" + collection +
                ", map=" + map +
                ", supertypeMap=" + supertypeMap +
                ", nestedObject=" + nestedObject +
                '}';
    }
}

/**
 * Nested class.
 */
class Price {
    private final float netPrice;
    private final float grossPrice;

    public Price(final float netPrice, final float grossPrice) {
        this.netPrice = netPrice;
        this.grossPrice = grossPrice;
    }

    public float getNetPrice() {
        return netPrice;
    }

    public float getGrossPrice() {
        return grossPrice;
    }
}
