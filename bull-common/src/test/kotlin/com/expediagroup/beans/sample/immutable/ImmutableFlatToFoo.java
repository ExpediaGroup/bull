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
package com.expediagroup.beans.sample.immutable;

import java.math.BigInteger;
import java.util.Arrays;

/**
 * Sample immutable object containing properties available in the source object inside other objects.
 * <p>
 * e.g. phoneNumbers field is available inside <code>FromFoo.nestedObject.phoneNumbers</code>
 * </p>
 */
public class ImmutableFlatToFoo {
    private final String name;
    private final BigInteger id;
    private final int[] phoneNumbers;

    public ImmutableFlatToFoo(final String name, final BigInteger id, final int[] phoneNumbers) {
        this.name = name;
        this.id = id;
        this.phoneNumbers = phoneNumbers;
    }

    public String getName() {
        return name;
    }

    public BigInteger getId() {
        return id;
    }

    public int[] getPhoneNumbers() {
        return phoneNumbers;
    }

    @Override
    public String toString() {
        return "ImmutableFlatToFoo{" +
                "name='" + name + '\'' +
                ", id=" + id +
                ", phoneNumbers=" + Arrays.toString(phoneNumbers) +
                '}';
    }
}
