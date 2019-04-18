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

import static org.testng.AssertJUnit.assertSame;

import org.junit.Test;

import com.hotels.beans.sample.FromFooWithBuilder;

public class BuilderTest {

    /**
     * Test that we can get the Builder from a class (with a builder) created manually
     */
    @Test
    public void checkDeclaredClassInManualBuilder() {
        // WHEN
        Class<?>[] declaredClasses = BuilderToFoo.class.getDeclaredClasses();
        // TODO check for null value...
        Class clazz  = declaredClasses[0];

        // THEN
        assertSame(clazz, BuilderToFoo.Builder.class);
    }

    /**
     * Test that we can get the Builder from a class created with Lombok
     * Expected name for the Builder created from Lombok  is "OriginalClassName" + "Builder" without any character in between
     * see https://projectlombok.org/features/Builder
     */

    @Test
    public void checkDeclaredClassInBuilderFromLombok() {
        // WHEN
        Class<?>[] declaredClasses = FromFooWithBuilder.class.getDeclaredClasses();
        Class clazz  = declaredClasses[0];

        // THEN
        assertSame(clazz, FromFooWithBuilder.FromFooWithBuilderBuilder.class);
    }
}

