/**
 * Copyright (C) 2019 Expedia, Inc.
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

package com.hotels.beans;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.MockitoAnnotations.initMocks;

import static com.shazam.shazamcrest.matcher.Matchers.sameBeanAs;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.mockito.InjectMocks;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.hotels.beans.error.InvalidBeanException;
import com.hotels.beans.sample.FromFooSimple;
import com.hotels.beans.sample.immutable.ImmutableToFoo;
import com.hotels.beans.sample.immutable.ImmutableToFooMissingCustomAnnotation;
import com.hotels.beans.sample.immutable.ImmutableToFooSimple;
import com.hotels.beans.transformer.Transformer;

/**
 * Unit test for {@link BeanUtils}.
 */
public class BeanUtilsTest {
    private static final BigInteger ID = new BigInteger("1234");
    private static final String NAME = "Goofy";
    private static final boolean ACTIVE = true;

    /**
     * The class to be tested.
     */
    @InjectMocks
    private BeanUtils underTest;

    /**
     * Initialized mocks.
     */
    @BeforeClass
    public void beforeClass() {
        initMocks(this);
    }

    /**
     * Test that a Transformer is returned.
     */
    @Test
    public void testGetTransformerWorksProperly() {
        //GIVEN

        //WHEN
        final Transformer transformer = underTest.getTransformer();

        //THEN
        assertNotNull(transformer);
    }

    /**
     * Test that an exception is returned if the given transformer is null.
     */
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testGetTransformerThrowsExceptionIfTheBeanTransformerIsNull() {
        //GIVEN

        //WHEN
        underTest.getTransformer(null, ImmutableToFooSimple.class);
    }

    /**
     * Test that the transformer function returned is able to transform the given object.
     * @param testCaseDescription the test case description
     * @param transformerFunction the transform function to use
     */
    @Test(dataProvider = "dataStaticTransformationTesting")
    public void testGetTransformerFunctionWorksProperly(final String testCaseDescription, final Function<FromFooSimple, ImmutableToFooSimple> transformerFunction) {
        //GIVEN
        FromFooSimple fromFooSimple = createFromFooSimple();
        List<FromFooSimple> fromFooSimpleList = Arrays.asList(fromFooSimple, fromFooSimple);

        //WHEN
        List<ImmutableToFooSimple> actual = fromFooSimpleList.stream()
                .map(transformerFunction)
                .collect(Collectors.toList());

        //THEN
        assertNotNull(transformerFunction);
        IntStream.range(0, actual.size())
                .forEach(i -> assertThat(actual.get(i), sameBeanAs(fromFooSimpleList.get(i))));
    }

    /**
     * Creates the parameters to be used for testing the default transformation operations.
     * @return parameters to be used for testing the default transformation operations.
     */
    @DataProvider(parallel = true)
    private Object[][] dataStaticTransformationTesting() {
        BeanUtils beanUtils = new BeanUtils();
        return new Object[][] {
                {"Test that the transformer function returned is able to transform the given object.",
                        beanUtils.getTransformer(ImmutableToFooSimple.class)},
                {"Test that the transformer function returned is able to transform the given object with the given transformer.",
                        beanUtils.getTransformer(beanUtils.getTransformer(), ImmutableToFooSimple.class)}
        };
    }

    /**
     * Test that no exceptions are thrown when the bean is valid.
     */
    @Test
    public void testValidateWorksProperly() {
        //GIVEN
        ImmutableToFooMissingCustomAnnotation validBean =
                new ImmutableToFooMissingCustomAnnotation(NAME, ID.intValue());

        //WHEN
        underTest.getValidator().validate(validBean);
    }

    /**
     * Test that an exception is returned if the given bean is not valid.
     */
    @Test(expectedExceptions = InvalidBeanException.class)
    public void testValidateThrowsExceptionIfTheGivenBeanIsInvalid() {
        //GIVEN
        ImmutableToFoo immutableToFoo =
                new ImmutableToFoo(null, null, null, null, null);

        //WHEN
        underTest.getValidator().validate(immutableToFoo);
    }

    /**
     * Creates a {@link FromFooSimple} instance.
     * @return the {@link FromFooSimple} instance.
     */
    private FromFooSimple createFromFooSimple() {
        return new FromFooSimple(NAME, ID, ACTIVE);
    }
}
