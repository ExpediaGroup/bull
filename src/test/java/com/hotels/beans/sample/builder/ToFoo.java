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

import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;

@Getter
@Setter
public class ToFoo {
    private String name;
    private BigInteger id;

    private ToFoo() {}

    // getters

    public static class Builder {
        private String name;
        private BigInteger id;

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public Builder withId(BigInteger id) {
            this.id = id;
            return this;
        }

        public ToFoo build() {
            ToFoo toFoo = new ToFoo();
            toFoo.id = this.id;
            toFoo.name = this.name;
            return toFoo;
        }
    }
}