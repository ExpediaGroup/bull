/**
 * Copyright (C) 2019 Expedia Inc.
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

package com.hotels.beans.sample.builder;

import java.math.BigInteger;
import java.util.List;

import com.hotels.beans.sample.mutable.MutableToSubFoo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public final class BuilderToFooWithTwoConstructor {
    private String name;
    private BigInteger id;
    private List<String> list;
    private List<MutableToSubFoo> nestedObjectList;
    private MutableToSubFoo nestedObject;

    /**
     * Private constructor.
     */
    private BuilderToFooWithTwoConstructor() {
    }

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

        public BuilderToFooWithTwoConstructor build() {
            BuilderToFooWithTwoConstructor builderToFoo = new BuilderToFooWithTwoConstructor();
            builderToFoo.id = this.id;
            builderToFoo.name = this.name;
            builderToFoo.list = this.list;
            builderToFoo.nestedObjectList = this.nestedObjectList;
            builderToFoo.nestedObject = this.nestedObject;
            return builderToFoo;
        }
    }
}
