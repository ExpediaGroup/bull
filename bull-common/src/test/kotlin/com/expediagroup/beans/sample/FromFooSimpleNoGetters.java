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

import java.math.BigInteger;

/**
 * Sample object without getter methods.
 */
public class FromFooSimpleNoGetters {
    public String name;
    public BigInteger id;
    public boolean active;

    public FromFooSimpleNoGetters() {
    }

    public FromFooSimpleNoGetters(final String name, final BigInteger id, final boolean active) {
        this.name = name;
        this.id = id;
        this.active = active;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public void setId(final BigInteger id) {
        this.id = id;
    }

    public void setActive(final boolean active) {
        this.active = active;
    }
}
