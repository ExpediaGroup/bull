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


/**
 * Sample class containing only primitive types.
 */
public class FromFooOnlyPrimitiveTypes {
    private String code;
    private int id;
    private float price;
    private String active;
    private Object uuid;

    public FromFooOnlyPrimitiveTypes() {
    }

    public FromFooOnlyPrimitiveTypes(final String code, final int id, final float price, final String active, final Object uuid) {
        this.code = code;
        this.id = id;
        this.price = price;
        this.active = active;
        this.uuid = uuid;
    }

    public String getCode() {
        return code;
    }

    public void setCode(final String code) {
        this.code = code;
    }

    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(final float price) {
        this.price = price;
    }

    public String getActive() {
        return active;
    }

    public void setActive(final String active) {
        this.active = active;
    }

    public Object getUuid() {
        return uuid;
    }

    public void setUuid(final Object uuid) {
        this.uuid = uuid;
    }
}
