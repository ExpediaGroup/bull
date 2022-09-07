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
package com.expediagroup.beans.sample.mixed;

import java.math.BigInteger;
import java.util.List;

import com.expediagroup.beans.sample.immutable.ImmutableToSubFoo;

/**
 * Mixed bean instantiable only through a Builder.
 */
public class MixedToFooWithBuilder {
    private final String name;
    private final List<ImmutableToSubFoo> nestedObjectList;
    private final List<String> list;
    private final ImmutableToSubFoo nestedObject;
    private final BigInteger id;

    public MixedToFooWithBuilder(final String name, final BigInteger id, final List<ImmutableToSubFoo> nestedObjectList, final List<String> list, final ImmutableToSubFoo nestedObject) {
        this.name = name;
        this.id = id;
        this.nestedObjectList = nestedObjectList;
        this.list = list;
        this.nestedObject = nestedObject;
    }

    public static MixedToFooWithBuilder.MixedToFooWithBuilderBuilder builder() {
        return new MixedToFooWithBuilder.MixedToFooWithBuilderBuilder();
    }

    public String getName() {
        return this.name;
    }

    public BigInteger getId() {
        return this.id;
    }

    public List<ImmutableToSubFoo> getNestedObjectList() {
        return this.nestedObjectList;
    }

    public List<String> getList() {
        return this.list;
    }

    public ImmutableToSubFoo getNestedObject() {
        return this.nestedObject;
    }

    public static class MixedToFooWithBuilderBuilder {
        private String name;
        private BigInteger id;
        private List<ImmutableToSubFoo> nestedObjectList;
        private List<String> list;
        private ImmutableToSubFoo nestedObject;

        MixedToFooWithBuilderBuilder() {
        }

        /**
         * @return {@code this}.
         */
        public MixedToFooWithBuilder.MixedToFooWithBuilderBuilder name(final String name) {
            this.name = name;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public MixedToFooWithBuilder.MixedToFooWithBuilderBuilder id(final BigInteger id) {
            this.id = id;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public MixedToFooWithBuilder.MixedToFooWithBuilderBuilder nestedObjectList(final List<ImmutableToSubFoo> nestedObjectList) {
            this.nestedObjectList = nestedObjectList;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public MixedToFooWithBuilder.MixedToFooWithBuilderBuilder list(final List<String> list) {
            this.list = list;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public MixedToFooWithBuilder.MixedToFooWithBuilderBuilder nestedObject(final ImmutableToSubFoo nestedObject) {
            this.nestedObject = nestedObject;
            return this;
        }

        public MixedToFooWithBuilder build() {
            return new MixedToFooWithBuilder(this.name, this.id, this.nestedObjectList, this.list, this.nestedObject);
        }

        @Override
        public String toString() {
            return "MixedToFooWithBuilder.MixedToFooWithBuilderBuilder(name=" + this.name + ", id=" + this.id + ", nestedObjectList=" + this.nestedObjectList + ", list=" + this.list + ", nestedObject=" + this.nestedObject + ")";
        }
    }
}
