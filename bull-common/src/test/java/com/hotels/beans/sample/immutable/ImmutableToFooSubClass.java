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

package com.hotels.beans.sample.immutable;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import javax.validation.constraints.NotNull;

import lombok.Getter;

/**
 * Sample immutable object extending a class.
 */
@Getter
public class ImmutableToFooSubClass extends ImmutableToFoo {
    @NotNull
    private final String surname;
    @NotNull
    private final int phone;
    @NotNull
    private final boolean check;
    @NotNull
    private final BigDecimal amount;

    /**
     * Constructor.
     */
    public ImmutableToFooSubClass(final String name, final BigInteger id, final List<String> list,
        final List<ImmutableToSubFoo> nestedObjectList, final ImmutableToSubFoo nestedObject, final String surname,
        final int phone, final boolean check, final BigDecimal amount) {
        super(name, id, list, nestedObjectList, nestedObject);
        this.surname = surname;
        this.phone = phone;
        this.check = check;
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "ImmutableToFooSubClass";
    }
}
