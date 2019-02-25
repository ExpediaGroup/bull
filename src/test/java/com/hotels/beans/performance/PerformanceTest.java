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

package com.hotels.beans.performance;

import static java.lang.String.valueOf;
import static java.lang.Thread.sleep;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

import static org.junit.Assert.assertTrue;
import static org.mockito.MockitoAnnotations.initMocks;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import org.apache.commons.lang3.time.StopWatch;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.mockito.InjectMocks;

import com.hotels.beans.BeanUtils;
import com.hotels.beans.sample.FromFoo;
import com.hotels.beans.sample.FromFooSimple;
import com.hotels.beans.sample.FromFooSubClass;
import com.hotels.beans.sample.FromSubFoo;
import com.hotels.beans.sample.immutable.ImmutableToFooSimple;
import com.hotels.beans.sample.immutable.ImmutableToFooSubClass;
import com.hotels.beans.sample.mixed.MixedToFoo;
import com.hotels.beans.sample.mutable.MutableToFooSimple;
import com.hotels.beans.sample.mutable.MutableToFooSubClass;
import com.hotels.beans.transformer.Transformer;

import lombok.extern.slf4j.Slf4j;

/**
 * Unit test for {@link BeanUtils}.
 */
@Slf4j
@RunWith(value = Parameterized.class)
public class PerformanceTest {
    private static final BigInteger ID = new BigInteger("1234");
    private static final String ITEM_1 = "donald";
    private static final String ITEM_2 = "duck";
    private static final String NAME = "Goofy";
    private static final String SURNAME = "surname";
    private static final int PHONE = 123456;
    private static final boolean CHECK = true;
    private static final BigDecimal AMOUNT = new BigDecimal(10);
    private static final String SUB_FOO_NAME = "Smith";
    private static final int[] SUB_FOO_PHONE_NUMBERS = {12345, 6892, 10873};
    private static final double TOTAL_TRANSFORMATIONS = 1000;
    private static final int TOTAL_SIMPLE_OBJECTS = 15;
    private static final int TOTAL_COMPLEX_OBJECTS = 5;
    private static final double SIMPLE_OBJECTS_MAX_TRANSFORMATION_TIME = 0.2;
    private static final double COMPLEX_OBJECTS_MAX_TRANSFORMATION_TIME = 1;
    private static final int WAIT_INTERVAL = 1000;
    private static final int NO_WAIT_INTERVAL = 0;

    private static FromFoo fromFoo;
    private static FromFooSimple fromFooSimple;
    private static List<FromSubFoo> fromSubFooList;
    private static List<String> sourceFooSimpleList;
    private static FromSubFoo fromSubFoo;
    private static FromFooSubClass fromFooSubClass;

    private final double totalTransformation;
    private final int waitInterval;
    private final Object sourceObject;
    private final Class<Object> destObjectClass;
    private final double maxTransformationTime;
    private final boolean disableValidation;

    /**
     * The class to be tested.
     */
    @InjectMocks
    private BeanUtils underTest;

    /**
     * All args constructor.
     * @param totalTransformation Total transformation to be performed
     * @param waitInterval time between one transformation and the other
     * @param sourceObject the Source object to copy
     * @param destObjectClass the total destination object
     * @param disableValidation disables the java bean validation
     * @param maxTransformationTime expected maximum transformation time
     */
    public PerformanceTest(final double totalTransformation, final int waitInterval, final Object sourceObject,
        final Class<Object> destObjectClass, final boolean disableValidation, final double maxTransformationTime) {
        this.totalTransformation = totalTransformation;
        this.waitInterval = waitInterval;
        this.sourceObject = sourceObject;
        this.destObjectClass = destObjectClass;
        this.disableValidation = disableValidation;
        this.maxTransformationTime = maxTransformationTime;
    }

    /**
     * Initialized mocks.
     */
    @Before
    public void beforeMethod() {
        initMocks(this);
        warmUp();
    }

    /**
     * Test that the bean copy gets completed by the expected time.
     * @throws InterruptedException
     */
    @Test
    public void testCopyPropertiesGetsCompletedInTheExpectedTime() throws InterruptedException {
        //GIVEN

        //WHEN
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        Transformer beanTransformer = underTest.getTransformer().setValidationDisabled(disableValidation);
        for (int i = 0; i <= totalTransformation; i++) {
            stopWatch.suspend();
            sleep(waitInterval);
            stopWatch.resume();
            beanTransformer.transform(sourceObject, destObjectClass);
        }
        stopWatch.stop();
        double avgTransformationTime = stopWatch.getTime(MILLISECONDS) / totalTransformation;

        //THEN
        log.info("Object: {}, Average transformation time: {} ms", destObjectClass, avgTransformationTime);
        assertTrue("Performance degradation! Expected: " + maxTransformationTime + ", actual: " + avgTransformationTime, avgTransformationTime <= maxTransformationTime);
    }

    /**
     * Warm up transformation.
     */
    private void warmUp() {
        underTest.getTransformer().transform(sourceObject, destObjectClass);
    }

    /**
     * Create an instance of two objects: one without custom annotation and another one with custom annotations then execute the copy into a specular immutable object.
     */
    private static void initObjects() {
        Map<String, String> subFooSampleMap = new HashMap<>();
        Map<String, List<String>> subFooComplexMap = new HashMap<>();
        Map<String, Map<String, String>> subFooVeryComplexMap = new HashMap<>();

        subFooSampleMap.put(ITEM_1, ITEM_2);
        subFooComplexMap.put(ITEM_1, singletonList(ITEM_2));
        subFooVeryComplexMap.put(ITEM_1, subFooSampleMap);
        fromSubFoo = new FromSubFoo(SUB_FOO_NAME, SUB_FOO_PHONE_NUMBERS, subFooSampleMap, subFooComplexMap, subFooVeryComplexMap);
        sourceFooSimpleList = new ArrayList<>();
        IntStream.range(0, TOTAL_SIMPLE_OBJECTS).parallel().forEach(i -> sourceFooSimpleList.add(valueOf(i)));
        fromSubFooList = new ArrayList<>();
        IntStream.range(0, TOTAL_COMPLEX_OBJECTS).parallel().forEach(i -> fromSubFooList.add(fromSubFoo));
        fromFoo = createFromFoo();
        fromFooSimple = createFromFooSimple();
        fromFooSubClass = createFromFooSubClass();
    }

    /**
     * Creates a {@link FromFoo} instance.
     * @return the {@link FromFoo} instance.
     */
    private static FromFoo createFromFoo() {
        return new FromFoo(NAME, ID, fromSubFooList, sourceFooSimpleList, fromSubFoo);
    }

    /**
     * Creates a {@link FromFooSimple} instance.
     * @return the {@link FromFooSimple} instance.
     */
    private static FromFooSimple createFromFooSimple() {
        return new FromFooSimple(NAME, ID);
    }

    /**
     * Creates a {@link FromFooSubClass} instance.
     * @return the {@link FromFooSubClass} instance.
     */
    private static FromFooSubClass createFromFooSubClass() {
        return new FromFooSubClass(fromFoo.getName(), fromFoo.getId(), fromFoo.getNestedObjectList(), fromFoo.getList(), fromFoo.getNestedObject(), SURNAME, PHONE, CHECK, AMOUNT);
    }

    /**
     * Data provider for the required test cases.
     * @return the test cases.
     */
    @Parameterized.Parameters(name = "{index}. Number of iterations:{0}, Wait interval: {1}, Expected max transformation time: {2},"
            + " Source object:{3}, Destination object: {4}.")
    public static Collection<Object[]> dataProvider() {
        initObjects();
        return asList(new Object[][]{
                {TOTAL_TRANSFORMATIONS, NO_WAIT_INTERVAL, fromFooSimple, MutableToFooSimple.class, true, SIMPLE_OBJECTS_MAX_TRANSFORMATION_TIME},
                {TOTAL_TRANSFORMATIONS, NO_WAIT_INTERVAL, fromFooSubClass, MutableToFooSubClass.class, true, COMPLEX_OBJECTS_MAX_TRANSFORMATION_TIME},
                {TOTAL_TRANSFORMATIONS, NO_WAIT_INTERVAL, fromFooSimple, ImmutableToFooSimple.class, true, SIMPLE_OBJECTS_MAX_TRANSFORMATION_TIME},
                {TOTAL_TRANSFORMATIONS, NO_WAIT_INTERVAL, fromFooSubClass, ImmutableToFooSubClass.class, true, COMPLEX_OBJECTS_MAX_TRANSFORMATION_TIME},
                {TOTAL_TRANSFORMATIONS, NO_WAIT_INTERVAL, fromFoo, MixedToFoo.class, false, COMPLEX_OBJECTS_MAX_TRANSFORMATION_TIME}
        });
    }
}
