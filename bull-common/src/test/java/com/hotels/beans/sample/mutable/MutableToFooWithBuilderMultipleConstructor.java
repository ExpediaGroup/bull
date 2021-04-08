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

import java.math.BigInteger;
import java.util.List;

import lombok.Getter;

/**
 * Mutable object instantiable only through a Builder.
 * The Builder class contains multiple constructors.
 */
@Getter
public final class MutableToFooWithBuilderMultipleConstructor {
    private String name;
    private BigInteger id;
    private List<String> list;
    private List<MutableToSubFoo> nestedObjectList;
    private MutableToSubFoo nestedObject;

    /**
     * Private constructor.
     */
    private MutableToFooWithBuilderMultipleConstructor() {
    }

    /**
     * Builder class.
     */
    static class Builder {
        private String name;
        private BigInteger id;
        private List<String> list;
        private List<MutableToSubFoo> nestedObjectList;
        private MutableToSubFoo nestedObject;

        Builder(final String name) {
            this.name = name;
        }

        Builder() {
        }

        public Builder withName(final String name) {
            this.name = name;
            return this;
        }

        public Builder withId(final BigInteger id) {
            this.id = id;
            return this;
        }

        public MutableToFooWithBuilderMultipleConstructor build() {
            MutableToFooWithBuilderMultipleConstructor builderToFoo = new MutableToFooWithBuilderMultipleConstructor();
            builderToFoo.id = this.id;
            builderToFoo.name = this.name;
            builderToFoo.list = this.list;
            builderToFoo.nestedObjectList = this.nestedObjectList;
            builderToFoo.nestedObject = this.nestedObject;
            return builderToFoo;
        }
    }
}
