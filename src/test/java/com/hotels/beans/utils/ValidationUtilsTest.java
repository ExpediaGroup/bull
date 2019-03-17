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

import static java.util.Objects.nonNull;

import static org.mockito.MockitoAnnotations.initMocks;

import org.mockito.InjectMocks;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

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
    @BeforeMethod
    public void beforeMethod() {
        initMocks(this);
    }

    /**
     * Test that an exception is thrown when the given parameter is null.
     * @param testCaseDescription the test case description
     * @param elemToCheck the element to check
     * @param exceptionMessage the exception message
     */
    @Test(dataProvider = "dataIllegalArgumentExceptionTesting", expectedExceptions = IllegalArgumentException.class)
    public void testNotNullRaisesAnExceptionWhenTheGivenObjectIsNull(final String testCaseDescription, final Object elemToCheck, final String exceptionMessage) {
        //GIVEN

        //WHEN
        if (nonNull(exceptionMessage)) {
            underTest.notNull(elemToCheck, exceptionMessage);
        } else {
            underTest.notNull(elemToCheck);
        }
    }

    /**
     * Creates the parameters to be used for testing tat the method {@code notNull} raises an {@link IllegalArgumentException}.
     * @return parameters to be used for testing tat the method {@code notNull} raises an {@link IllegalArgumentException}.
     */
    @DataProvider
    private Object[][] dataIllegalArgumentExceptionTesting() {
        return new Object[][] {
                {"Tests that the method raise an exception if the given parameter is null and a description message is defined", null, EXCEPTION_MESSAGE},
                {"Tests that the method raise an exception if the given parameter is null and a description message is not defined", null, null}
        };
    }

    /**
     * Test that no exception are throw when the given parameter is not null.
     * @param testCaseDescription the test case description
     * @param elemToCheck the element to check
     * @param exceptionMessage the exception message
     */
    @Test(dataProvider = "dataNoExceptionAreRaisedTesting")
    public void testThatNoExceptionAreThrownWhenTheGivenObjectIsNotNullEvenWithoutACustomMessage(final String testCaseDescription, final Object elemToCheck,
        final String exceptionMessage) {
        //GIVEN

        //WHEN
        if (nonNull(exceptionMessage)) {
            underTest.notNull(elemToCheck, exceptionMessage);
        } else {
            underTest.notNull(elemToCheck);
        }
    }

    /**
     * Creates the parameters to be used for testing tat the method {@code notNull} raises an {@link IllegalArgumentException}.
     * @return parameters to be used for testing tat the method {@code notNull} raises an {@link IllegalArgumentException}.
     */
    @DataProvider
    private Object[][] dataNoExceptionAreRaisedTesting() {
        return new Object[][] {
                {"Tests that the method does not raise an exception if the given parameter is not null and a description message is defined", VALUE, EXCEPTION_MESSAGE},
                {"Tests that the method does not raise an exception if the given parameter is not null and a description message is not defined", VALUE, null}
        };
    }
}
