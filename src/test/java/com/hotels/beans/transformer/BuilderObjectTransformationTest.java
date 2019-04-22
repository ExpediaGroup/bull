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

package com.hotels.beans.transformer;

import static org.mockito.MockitoAnnotations.initMocks;
import static org.junit.Assert.assertThat;

import static com.shazam.shazamcrest.matcher.Matchers.sameBeanAs;

import org.mockito.InjectMocks;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

//import com.hotels.beans.sample.builder.BuilderToFooWithTwoConstructor;
import com.hotels.beans.sample.builder.BuilderToFoo;
import com.hotels.beans.sample.builder.LombokBuilderToFoo;


/**
 * Unit test for all {@link Transformer} functions related to Object based on Builder Pattern.
 */
public class BuilderObjectTransformationTest extends AbstractTransformerTest {
    /**
     * The class to be tested.
     */
    @InjectMocks
    private TransformerImpl underTest;

    /**
     * Initialized mocks.
     */
    @BeforeMethod
    public void beforeMethod() {
        initMocks(this);
    }

    /**
     * Test mutable beans are correctly copied.
     */
    @Test
    public void testBuilderWithLombok() {
        //GIVEN

        //WHEN
        LombokBuilderToFoo actual = underTest.transform(fromFoo, LombokBuilderToFoo.class);

        //THEN
        assertThat(actual, sameBeanAs(fromFoo));
    }

    @Test
    public void testManualBuilder() {
        //GIVEN

        //WHEN
        BuilderToFoo actual = underTest.transform(fromFoo, BuilderToFoo.class);

        //THEN
        assertThat(actual, sameBeanAs(fromFoo));
    }

//    @Test
//    public void testManualBuilderWithTwoContructor() {
//        //GIVEN
//
//        //WHEN
//        BuilderToFooWithTwoConstructor actual = underTest.transform(fromFoo, BuilderToFooWithTwoConstructor.class);
//
//        //THEN
//        assertThat(actual, sameBeanAs(fromFoo));
//    }
}
