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
package com.expediagroup.transformer.validator;

import static java.math.BigInteger.ONE;
import static java.math.BigInteger.ZERO;
import static java.util.Objects.nonNull;

import static org.mockito.MockitoAnnotations.openMocks;
import static org.testng.Assert.assertEquals;

import java.math.BigInteger;
import java.util.Collections;
import java.util.List;

import org.mockito.InjectMocks;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.expediagroup.beans.sample.mixed.MixedToFoo;
import com.expediagroup.transformer.error.InvalidBeanException;

/**
 * Unit test for {@link Validator}.
 */
public class ValidatorTest {
    /**
     * Exception message.
     */
    private static final String EXCEPTION_MESSAGE = "exception message";

    /**
     * A sample ID.
     */
    private static final BigInteger ID = new BigInteger("1234");

    /**
     * A sample name.
     */
    private static final String NAME = "Goofy";

    /**
     * A sample value.
     */
    private static final String VALUE = "val";

    /**
     * The class to be tested.
     */
    @InjectMocks
    private ValidatorImpl underTest;

    /**
     * Initialized mocks.
     */
    @BeforeClass
    public void beforeClass() {
        openMocks(this);
    }

    /**
     * Test that an exception is thrown when the given parameter is null.
     * @param testCaseDescription the test case description
     * @param elemToCheck the element to check
     * @param exceptionMessage the exception message
     */
    @Test(dataProvider = "dataIllegalArgumentExceptionTesting", expectedExceptions = IllegalArgumentException.class)
    public void testNotNullRaisesAnExceptionWhenTheGivenObjectIsNull(final String testCaseDescription, final Object elemToCheck, final String exceptionMessage) {
        // GIVEN

        // WHEN
        if (nonNull(exceptionMessage)) {
            Validator.notNull(elemToCheck, exceptionMessage);
        } else {
            Validator.notNull(elemToCheck);
        }
    }

    /**
     * Creates the parameters to be used for testing that the method {@code notNull} raises an {@link IllegalArgumentException}.
     * @return parameters to be used for testing that the method {@code notNull} raises an {@link IllegalArgumentException}.
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
        // GIVEN

        // WHEN
        if (nonNull(exceptionMessage)) {
            Validator.notNull(elemToCheck, exceptionMessage);
        } else {
            Validator.notNull(elemToCheck);
        }
    }

    /**
     * Creates the parameters to be used for testing that the method {@code notNull} raises an {@link IllegalArgumentException}.
     * @return parameters to be used for testing that the method {@code notNull} raises an {@link IllegalArgumentException}.
     */
    @DataProvider
    private Object[][] dataNoExceptionAreRaisedTesting() {
        return new Object[][] {
                {"Tests that the method does not raise an exception if the given parameter is not null and a description message is defined", VALUE, EXCEPTION_MESSAGE},
                {"Tests that the method does not raise an exception if the given parameter is not null and a description message is not defined", VALUE, null}
        };
    }

    /**
     * Test that an {@link InvalidBeanException} is thrown if the bean is not valid.
     */
    @Test(expectedExceptions = InvalidBeanException.class)
    public void testValidateThrowsExceptionWhenTheBeanIsInvalid() {
        // GIVEN
        MixedToFoo validBean = createTestBean(null);

        // WHEN
        underTest.validate(validBean);
    }

    /**
     * Test that no exceptions are thrown if the bean is valid.
     */
    @Test
    public void testValidateDoesNotThrowsExceptionWhenTheBeanIsValid() {
        // GIVEN
        MixedToFoo validBean = createTestBean(ID);

        // WHEN
        underTest.validate(validBean);
    }

    /**
     * Test that the method {@code getConstraintViolationsAsString} returns the exact number of violations.
     * @param testCaseDescription the test case description
     * @param elemToCheck the class to validate
     * @param totalExpectedViolation the exception message
     */
    @Test(dataProvider = "dataValidationConstraintsList")
    public void testGetConstraintViolationsWorksAsExpected(final String testCaseDescription, final Object elemToCheck, final int totalExpectedViolation) {
        // GIVEN

        // WHEN
        List<String> actual = underTest.getConstraintViolationsMessages(elemToCheck);

        // THEN
        assertEquals(totalExpectedViolation, actual.size());
    }

    /**
     * Creates the parameters to be used for testing that the method {@code getConstraintViolationsAsString} returns the exact number of violations.
     * @return parameters to be used for testing that the method {@code getConstraintViolationsAsString} returns the exact number of violations.
     */
    @DataProvider
    private Object[][] dataValidationConstraintsList() {
        return new Object[][] {
                {"Tests that no constraints violation are returned if the object is valid.", createTestBean(ID), ZERO.intValue()},
                {"Tests that one constraints violation is returned if the object id is null.", createTestBean(null), ONE.intValue()}
        };
    }

    /**
     * Creates a test bean.
     * @param id the id to use
     * @return a populated {@link MixedToFoo}
     */
    private MixedToFoo createTestBean(final BigInteger id) {
        return new MixedToFoo(id, NAME, Collections.emptyList(), null, null);
    }
}
