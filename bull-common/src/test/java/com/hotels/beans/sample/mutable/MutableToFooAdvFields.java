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
package com.hotels.beans.sample.mutable;

import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import com.hotels.beans.sample.ISubClass;
import com.hotels.transformer.constant.ClassType;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Sample mutable object containing special fields.
 */
@Getter
@Setter
@ToString
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

    private void setIndex(final String indexNumber) {
        this.indexNumber = indexNumber;
    }
}
