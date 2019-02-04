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

package com.hotels.beans.utils;

import static org.mockito.MockitoAnnotations.initMocks;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;

/**
 * Unit test for {@link ValidationUtils}.
 */
public class ValidationUtilsTest {
    /**
     * Exception message.
     */
    private static final String EXCEPTION_MESSAGE = "exception message";

    /**
     * A sample value.
     */
    private static final String VALUE = "val";

    /**
     * The class to be tested.
     */
    @InjectMocks
    private static ValidationUtils underTest;

    /**
     * Initialized mocks.
     */
    @Before
    public void beforeMethod() {
        initMocks(this);
    }

    /**
     * Test that an exception is thrown when the given parameter is null.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNotNullRaisesAnExceptionWhenTheGivenObjectIsNull() {
        //GIVEN

        //WHEN
        underTest.notNull(null, EXCEPTION_MESSAGE);
    }

    /**
     * Test that no exception are throw when the given parameter is not null.
     */
    @Test
    public void testThatNoExceptionAreThrownWhenTheGivenObjectIsNotNull() {
        //GIVEN

        //WHEN
        underTest.notNull(VALUE, EXCEPTION_MESSAGE);
    }

    /**
     * Test that an exception is thrown when the given parameter is null.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNotNullRaisesAnExceptionWhenTheGivenObjectIsNullEvenWithoutACustomMessage() {
        //GIVEN

        //WHEN
        underTest.notNull(null);
    }

    /**
     * Test that no exception are throw when the given parameter is not null.
     */
    @Test
    public void testThatNoExceptionAreThrownWhenTheGivenObjectIsNotNullEvenWithoutACustomMessage() {
        //GIVEN

        //WHEN
        underTest.notNull(VALUE);
    }
}
