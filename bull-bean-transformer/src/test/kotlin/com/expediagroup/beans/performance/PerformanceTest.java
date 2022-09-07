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
package com.expediagroup.beans.performance;

import static java.lang.String.valueOf;
import static java.lang.Thread.sleep;
import static java.util.Collections.singletonList;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.MockitoAnnotations.openMocks;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import org.apache.commons.lang3.time.StopWatch;
import org.mockito.InjectMocks;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.expediagroup.beans.BeanUtils;
import com.expediagroup.beans.sample.FromFoo;
import com.expediagroup.beans.sample.FromFooSimple;
import com.expediagroup.beans.sample.FromFooSubClass;
import com.expediagroup.beans.sample.FromSubFoo;
import com.expediagroup.beans.sample.immutable.ImmutableToFooSimple;
import com.expediagroup.beans.sample.immutable.ImmutableToFooSubClass;
import com.expediagroup.beans.sample.mixed.MixedToFoo;
import com.expediagroup.beans.sample.mutable.MutableToFooSimple;
import com.expediagroup.beans.sample.mutable.MutableToFooSubClass;
import com.expediagroup.beans.transformer.BeanTransformer;

import lombok.extern.slf4j.Slf4j;

/**
 * Unit test for {@link BeanUtils}.
 */
@Slf4j
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
    private static final boolean ACTIVE = true;

    private static FromFoo fromFoo;
    private static FromFooSimple fromFooSimple;
    private static List<FromSubFoo> fromSubFooList;
    private static List<String> sourceFooSimpleList;
    private static FromSubFoo fromSubFoo;
    private static FromFooSubClass fromFooSubClass;

    /**
     * The class to be tested.
     */
    @InjectMocks
    private BeanUtils underTest;

    /**
     * Initialized data provider objects.
     */
    @BeforeClass
    public void beforeClass() {
        initObjects();
        openMocks(this);
    }

    /**
     * Test that the bean copy gets completed by the expected time.
     * @param totalTransformation Total transformation to be performed
     * @param waitInterval time between one transformation and the other
     * @param sourceObject the Source object to copy
     * @param destObjectClass the total destination object
     * @param maxTransformationTime expected maximum transformation time
     * @throws InterruptedException if an error occurs during the thread sleep
     */
    @Test(dataProvider = "dataPerformanceTest")
    public void testCopyPropertiesGetsCompletedInTheExpectedTime(final double totalTransformation, final int waitInterval,
        final Object sourceObject, final Class<Object> destObjectClass, final double maxTransformationTime) throws InterruptedException {
        // GIVEN
        warmUp(sourceObject, destObjectClass);

        // WHEN
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        BeanTransformer transformer = underTest.getTransformer();
        for (int i = 0; i <= totalTransformation; i++) {
            stopWatch.suspend();
            sleep(waitInterval);
            stopWatch.resume();
            transformer.transform(sourceObject, destObjectClass);
        }
        stopWatch.stop();
        double avgTransformationTime = stopWatch.getTime(MILLISECONDS) / totalTransformation;

        // THEN
        log.info("Object: {}, Average transformation time: {} ms", destObjectClass, avgTransformationTime);
        assertThat(avgTransformationTime)
                .as("Performance degradation! Expected: %s, actual: %s", maxTransformationTime, avgTransformationTime)
                .isLessThanOrEqualTo(maxTransformationTime);
    }

    /**
     * Warm up transformation.
     * @param sourceObject the Source object to copy
     * @param destObjectClass the total destination object
     */
    private void warmUp(final Object sourceObject, final Class<Object> destObjectClass) {
        underTest.getTransformer().transform(sourceObject, destObjectClass);
    }

    /**
     * Create an instance of two objects: one without custom annotation and another one with custom annotations then execute the copy into a specular immutable object.
     */
    private void initObjects() {
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
    private FromFoo createFromFoo() {
        return new FromFoo(NAME, ID, fromSubFooList, sourceFooSimpleList, fromSubFoo);
    }

    /**
     * Creates a {@link FromFooSimple} instance.
     * @return the {@link FromFooSimple} instance.
     */
    private FromFooSimple createFromFooSimple() {
        return new FromFooSimple(NAME, ID, ACTIVE);
    }

    /**
     * Creates a {@link FromFooSubClass} instance.
     * @return the {@link FromFooSubClass} instance.
     */
    private FromFooSubClass createFromFooSubClass() {
        return new FromFooSubClass(fromFoo.getName(), fromFoo.getId(), fromFoo.getNestedObjectList(), fromFoo.getList(), fromFoo.getNestedObject(), SURNAME, PHONE, CHECK, AMOUNT);
    }

    /**
     * Data provider for the required test cases.
     * @return the test cases.
     */
    @DataProvider
    public Object[][] dataPerformanceTest() {
        return new Object[][]{
                {TOTAL_TRANSFORMATIONS, NO_WAIT_INTERVAL, fromFooSimple, MutableToFooSimple.class, SIMPLE_OBJECTS_MAX_TRANSFORMATION_TIME},
                {TOTAL_TRANSFORMATIONS, NO_WAIT_INTERVAL, fromFooSubClass, MutableToFooSubClass.class, COMPLEX_OBJECTS_MAX_TRANSFORMATION_TIME},
                {TOTAL_TRANSFORMATIONS, NO_WAIT_INTERVAL, fromFooSimple, ImmutableToFooSimple.class, SIMPLE_OBJECTS_MAX_TRANSFORMATION_TIME},
                {TOTAL_TRANSFORMATIONS, NO_WAIT_INTERVAL, fromFooSubClass, ImmutableToFooSubClass.class, COMPLEX_OBJECTS_MAX_TRANSFORMATION_TIME},
                {TOTAL_TRANSFORMATIONS, NO_WAIT_INTERVAL, fromFoo, MixedToFoo.class, COMPLEX_OBJECTS_MAX_TRANSFORMATION_TIME}
        };
    }
}
