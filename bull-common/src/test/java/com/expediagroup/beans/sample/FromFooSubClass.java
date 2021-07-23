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
package com.expediagroup.beans.sample;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import lombok.Getter;
import lombok.ToString;

/**
 * Sample sub class.
 */
@Getter
@ToString
public class FromFooSubClass extends FromFoo {
    private final String surname;
    private final int phone;
    private final boolean check;
    private final BigDecimal amount;

    public FromFooSubClass(final String name, final BigInteger id, final List<FromSubFoo> nestedObjectList, final List<String> list, final FromSubFoo nestedObject,
        final String surname, final int phone, final boolean check, final BigDecimal amount) {
        super(name, id, nestedObjectList, list, nestedObject);
        this.surname = surname;
        this.phone = phone;
        this.check = check;
        this.amount = amount;
    }
}
