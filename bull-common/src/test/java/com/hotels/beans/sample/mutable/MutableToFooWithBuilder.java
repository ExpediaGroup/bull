/**
 * Copyright (C) 2019-2020 Expedia, Inc.
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
import lombok.Setter;

/**
 * Mutable object instantiable only through a Builder.
 */
@Getter
@Setter
public final class MutableToFooWithBuilder {
    private String name;
    private BigInteger id;
    private List<String> list;
    private List<MutableToSubFoo> nestedObjectList;
    private MutableToSubFoo nestedObject;

    private MutableToFooWithBuilder() {
    }

    public static class Builder {
        private String name;
        private BigInteger id;
        private List<String> list;
        private List<MutableToSubFoo> nestedObjectList;
        private MutableToSubFoo nestedObject;

        public Builder withName(final String name) {
            this.name = name;
            return this;
        }

        public Builder withId(final BigInteger id) {
            this.id = id;
            return this;
        }

        public MutableToFooWithBuilder build() {
            MutableToFooWithBuilder mutableToFooWithBuilder = new MutableToFooWithBuilder();
            mutableToFooWithBuilder.id = this.id;
            mutableToFooWithBuilder.name = this.name;
            mutableToFooWithBuilder.list = this.list;
            mutableToFooWithBuilder.nestedObjectList = this.nestedObjectList;
            mutableToFooWithBuilder.nestedObject = this.nestedObject;
            return mutableToFooWithBuilder;
        }
    }
}
