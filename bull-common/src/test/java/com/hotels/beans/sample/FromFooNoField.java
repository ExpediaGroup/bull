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
package com.hotels.beans.sample;

import static java.lang.Boolean.TRUE;
import static java.math.BigInteger.ONE;

import java.math.BigInteger;

import lombok.ToString;

/**
 * Sample object without any field but with getter methods.
 */
@ToString
public class FromFooNoField {

    public String getName() {
        return "FromFooNoField";
    }

    public BigInteger getId() {
        return ONE;
    }

    public boolean isActive() {
        return TRUE;
    }
}
