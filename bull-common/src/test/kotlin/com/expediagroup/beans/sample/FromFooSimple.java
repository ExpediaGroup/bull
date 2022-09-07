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
import java.util.Objects;

/**
 * Sample immutable object.
 */
public class FromFooSimple {
    public String name;
    public BigInteger id;
    public boolean active;

    public FromFooSimple() {
    }

    public FromFooSimple(final String name, final BigInteger id, final boolean active) {
        this.name = name;
        this.id = id;
        this.active = active;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public BigInteger getId() {
        return id;
    }

    public void setId(final BigInteger id) {
        this.id = id;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(final boolean active) {
        this.active = active;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final FromFooSimple that = (FromFooSimple) o;
        return active == that.active && Objects.equals(name, that.name) && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, id, active);
    }

    @Override
    public String toString() {
        return "FromFooSimple{" +
                "name='" + name + '\'' +
                ", id=" + id +
                ", active=" + active +
                '}';
    }
}
