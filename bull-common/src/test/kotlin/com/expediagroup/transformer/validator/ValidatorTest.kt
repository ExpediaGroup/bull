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
package com.expediagroup.transformer.validator

import com.expediagroup.beans.sample.mixed.MixedToFoo
import com.expediagroup.transformer.error.InvalidBeanException
import org.mockito.InjectMocks
import org.mockito.MockitoAnnotations
import org.testng.Assert
import org.testng.annotations.BeforeClass
import org.testng.annotations.DataProvider
import org.testng.annotations.Test
import java.math.BigInteger
import java.util.*

/**
 * Unit test for [Validator].
 */
class ValidatorTest {
    /**
     * The class to be tested.
     */
    @InjectMocks
    private lateinit var underTest: ValidatorImpl

    /**
     * Initialized mocks.
     */
    @BeforeClass
    fun beforeClass() {
        MockitoAnnotations.openMocks(this)
    }

    /**
     * Test that an exception is thrown when the given parameter is null.
     * @param testCaseDescription the test case description
     * @param elemToCheck the element to check
     * @param exceptionMessage the exception message
     */
    @Test(dataProvider = "dataIllegalArgumentExceptionTesting", expectedExceptions = [IllegalArgumentException::class])
    fun testNotNullRaisesAnExceptionWhenTheGivenObjectIsNull(
        testCaseDescription: String?,
        elemToCheck: Any,
        exceptionMessage: String?
    ) {
        // GIVEN

        // WHEN
        if (Objects.nonNull(exceptionMessage)) {
            Validator.notNull(elemToCheck, exceptionMessage)
        } else {
            Validator.notNull(elemToCheck)
        }
    }

    /**
     * Creates the parameters to be used for testing that the method `notNull` raises an [IllegalArgumentException].
     * @return parameters to be used for testing that the method `notNull` raises an [IllegalArgumentException].
     */
    @DataProvider
    private fun dataIllegalArgumentExceptionTesting(): Array<Array<Any?>> {
        return arrayOf(
            arrayOf(
                "Tests that the method raise an exception if the given parameter is null and a description message is defined",
                null,
                EXCEPTION_MESSAGE
            ),
            arrayOf(
                "Tests that the method raise an exception if the given parameter is null and a description message is not defined",
                null,
                null
            )
        )
    }

    /**
     * Test that no exception are throw when the given parameter is not null.
     * @param testCaseDescription the test case description
     * @param elemToCheck the element to check
     * @param exceptionMessage the exception message
     */
    @Test(dataProvider = "dataNoExceptionAreRaisedTesting")
    fun testThatNoExceptionAreThrownWhenTheGivenObjectIsNotNullEvenWithoutACustomMessage(
        testCaseDescription: String?,
        elemToCheck: Any,
        exceptionMessage: String?
    ) {
        // GIVEN

        // WHEN
        if (Objects.nonNull(exceptionMessage)) {
            Validator.notNull(elemToCheck, exceptionMessage)
        } else {
            Validator.notNull(elemToCheck)
        }
    }

    /**
     * Creates the parameters to be used for testing that the method `notNull` raises an [IllegalArgumentException].
     * @return parameters to be used for testing that the method `notNull` raises an [IllegalArgumentException].
     */
    @DataProvider
    private fun dataNoExceptionAreRaisedTesting(): Array<Array<Any?>> {
        return arrayOf(
            arrayOf(
                "Tests that the method does not raise an exception if the given parameter is not null and a description message is defined",
                VALUE,
                EXCEPTION_MESSAGE
            ),
            arrayOf(
                "Tests that the method does not raise an exception if the given parameter is not null and a description message is not defined",
                VALUE,
                null
            )
        )
    }

    /**
     * Test that an [InvalidBeanException] is thrown if the bean is not valid.
     */
    @Test(expectedExceptions = [InvalidBeanException::class])
    fun testValidateThrowsExceptionWhenTheBeanIsInvalid() {
        // GIVEN
        val validBean = createTestBean(null)

        // WHEN
        underTest.validate(validBean)
    }

    /**
     * Test that no exceptions are thrown if the bean is valid.
     */
    @Test
    fun testValidateDoesNotThrowsExceptionWhenTheBeanIsValid() {
        // GIVEN
        val validBean = createTestBean(ID)

        // WHEN
        underTest.validate(validBean)
    }

    /**
     * Test that the method `getConstraintViolationsAsString` returns the exact number of violations.
     * @param testCaseDescription the test case description
     * @param elemToCheck the class to validate
     * @param totalExpectedViolation the exception message
     */
    @Test(dataProvider = "dataValidationConstraintsList")
    fun testGetConstraintViolationsWorksAsExpected(
        testCaseDescription: String?,
        elemToCheck: Any,
        totalExpectedViolation: Int
    ) {
        // GIVEN

        // WHEN
        val actual = underTest.getConstraintViolationsMessages(elemToCheck)

        // THEN
        Assert.assertEquals(totalExpectedViolation, actual.size)
    }

    /**
     * Creates the parameters to be used for testing that the method `getConstraintViolationsAsString` returns the exact number of violations.
     * @return parameters to be used for testing that the method `getConstraintViolationsAsString` returns the exact number of violations.
     */
    @DataProvider
    private fun dataValidationConstraintsList(): Array<Array<Any>> {
        return arrayOf(
            arrayOf(
                "Tests that no constraints violation are returned if the object is valid.", createTestBean(
                    ID
                ), BigInteger.ZERO.toInt()
            ),
            arrayOf(
                "Tests that one constraints violation is returned if the object id is null.",
                createTestBean(null),
                BigInteger.ONE.toInt()
            )
        )
    }

    /**
     * Creates a test bean.
     * @param id the id to use
     * @return a populated [MixedToFoo]
     */
    private fun createTestBean(id: BigInteger?): MixedToFoo {
        return MixedToFoo(id, NAME, emptyList(), null, null)
    }

    companion object {
        /**
         * Exception message.
         */
        private const val EXCEPTION_MESSAGE = "exception message"

        /**
         * A sample ID.
         */
        private val ID = BigInteger("1234")

        /**
         * A sample name.
         */
        private const val NAME = "Goofy"

        /**
         * A sample value.
         */
        private const val VALUE = "val"
    }
}