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
package com.expediagroup.beans.performance

import com.expediagroup.beans.BeanUtils
import com.expediagroup.beans.sample.FromFoo
import com.expediagroup.beans.sample.FromFooSimple
import com.expediagroup.beans.sample.FromFooSubClass
import com.expediagroup.beans.sample.FromSubFoo
import com.expediagroup.beans.sample.immutable.ImmutableToFooSimple
import com.expediagroup.beans.sample.immutable.ImmutableToFooSubClass
import com.expediagroup.beans.sample.mixed.MixedToFoo
import com.expediagroup.beans.sample.mutable.MutableToFooSimple
import com.expediagroup.beans.sample.mutable.MutableToFooSubClass
import lombok.extern.slf4j.Slf4j
import org.apache.commons.lang3.time.StopWatch
import org.assertj.core.api.Assertions
import org.mockito.InjectMocks
import org.mockito.MockitoAnnotations
import org.testng.annotations.BeforeClass
import org.testng.annotations.DataProvider
import org.testng.annotations.Test
import java.math.BigDecimal
import java.math.BigInteger
import java.util.concurrent.TimeUnit
import java.util.stream.IntStream

/**
 * Unit test for [BeanUtils].
 */
@Slf4j
class PerformanceTest {
    /**
     * The class to be tested.
     */
    @InjectMocks
    private val underTest: BeanUtils? = null

    /**
     * Initialized data provider objects.
     */
    @BeforeClass
    fun beforeClass() {
        initObjects()
        MockitoAnnotations.openMocks(this)
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
    @Throws(InterruptedException::class)
    fun testCopyPropertiesGetsCompletedInTheExpectedTime(
        totalTransformation: Double,
        waitInterval: Int,
        sourceObject: Any,
        destObjectClass: Class<Any>,
        maxTransformationTime: Double
    ) {
        // GIVEN
        warmUp(sourceObject, destObjectClass)

        // WHEN
        val stopWatch = StopWatch()
        stopWatch.start()
        val transformer = underTest!!.transformer
        var i = 0
        while (i <= totalTransformation) {
            stopWatch.suspend()
            Thread.sleep(waitInterval.toLong())
            stopWatch.resume()
            transformer.transform(sourceObject, destObjectClass)
            i++
        }
        stopWatch.stop()
        val avgTransformationTime = stopWatch.getTime(TimeUnit.MILLISECONDS) / totalTransformation

        // THEN
        PerformanceTest.log.info(
            "Object: {}, Average transformation time: {} ms",
            destObjectClass,
            avgTransformationTime
        )
        Assertions.assertThat(avgTransformationTime)
            .`as`("Performance degradation! Expected: %s, actual: %s", maxTransformationTime, avgTransformationTime)
            .isLessThanOrEqualTo(maxTransformationTime)
    }

    /**
     * Warm up transformation.
     * @param sourceObject the Source object to copy
     * @param destObjectClass the total destination object
     */
    private fun warmUp(sourceObject: Any, destObjectClass: Class<Any>) {
        underTest!!.transformer.transform(sourceObject, destObjectClass)
    }

    /**
     * Create an instance of two objects: one without custom annotation and another one with custom annotations then execute the copy into a specular immutable object.
     */
    private fun initObjects() {
        val subFooSampleMap: MutableMap<String, String> = HashMap()
        val subFooComplexMap: MutableMap<String, List<String>> = HashMap()
        val subFooVeryComplexMap: MutableMap<String, Map<String, String>> = HashMap()
        subFooSampleMap[ITEM_1] = ITEM_2
        subFooComplexMap[ITEM_1] = listOf(ITEM_2)
        subFooVeryComplexMap[ITEM_1] = subFooSampleMap
        fromSubFoo =
            FromSubFoo(SUB_FOO_NAME, SUB_FOO_PHONE_NUMBERS, subFooSampleMap, subFooComplexMap, subFooVeryComplexMap)
        sourceFooSimpleList = ArrayList()
        IntStream.range(0, TOTAL_SIMPLE_OBJECTS).parallel().forEach { i: Int -> sourceFooSimpleList.add(i.toString()) }
        fromSubFooList = ArrayList()
        IntStream.range(0, TOTAL_COMPLEX_OBJECTS).parallel().forEach { i: Int -> fromSubFooList.add(fromSubFoo) }
        fromFoo = createFromFoo()
        fromFooSimple = createFromFooSimple()
        fromFooSubClass = createFromFooSubClass()
    }

    /**
     * Creates a [FromFoo] instance.
     * @return the [FromFoo] instance.
     */
    private fun createFromFoo(): FromFoo {
        return FromFoo(NAME, ID, fromSubFooList, sourceFooSimpleList, fromSubFoo)
    }

    /**
     * Creates a [FromFooSimple] instance.
     * @return the [FromFooSimple] instance.
     */
    private fun createFromFooSimple(): FromFooSimple {
        return FromFooSimple(NAME, ID, ACTIVE)
    }

    /**
     * Creates a [FromFooSubClass] instance.
     * @return the [FromFooSubClass] instance.
     */
    private fun createFromFooSubClass(): FromFooSubClass {
        return FromFooSubClass(
            fromFoo!!.name,
            fromFoo!!.id,
            fromFoo!!.nestedObjectList,
            fromFoo!!.list,
            fromFoo!!.nestedObject,
            SURNAME,
            PHONE,
            CHECK,
            AMOUNT
        )
    }

    /**
     * Data provider for the required test cases.
     * @return the test cases.
     */
    @DataProvider
    fun dataPerformanceTest(): Array<Array<Any?>> {
        return arrayOf(
            arrayOf(
                TOTAL_TRANSFORMATIONS,
                NO_WAIT_INTERVAL,
                fromFooSimple,
                MutableToFooSimple::class.java,
                SIMPLE_OBJECTS_MAX_TRANSFORMATION_TIME
            ),
            arrayOf(
                TOTAL_TRANSFORMATIONS,
                NO_WAIT_INTERVAL,
                fromFooSubClass,
                MutableToFooSubClass::class.java,
                COMPLEX_OBJECTS_MAX_TRANSFORMATION_TIME
            ),
            arrayOf(
                TOTAL_TRANSFORMATIONS,
                NO_WAIT_INTERVAL,
                fromFooSimple,
                ImmutableToFooSimple::class.java,
                SIMPLE_OBJECTS_MAX_TRANSFORMATION_TIME
            ),
            arrayOf(
                TOTAL_TRANSFORMATIONS,
                NO_WAIT_INTERVAL,
                fromFooSubClass,
                ImmutableToFooSubClass::class.java,
                COMPLEX_OBJECTS_MAX_TRANSFORMATION_TIME
            ),
            arrayOf(
                TOTAL_TRANSFORMATIONS,
                NO_WAIT_INTERVAL,
                fromFoo,
                MixedToFoo::class.java,
                COMPLEX_OBJECTS_MAX_TRANSFORMATION_TIME
            )
        )
    }

    companion object {
        private val ID = BigInteger("1234")
        private const val ITEM_1 = "donald"
        private const val ITEM_2 = "duck"
        private const val NAME = "Goofy"
        private const val SURNAME = "surname"
        private const val PHONE = 123456
        private const val CHECK = true
        private val AMOUNT = BigDecimal(10)
        private const val SUB_FOO_NAME = "Smith"
        private val SUB_FOO_PHONE_NUMBERS = intArrayOf(12345, 6892, 10873)
        private const val TOTAL_TRANSFORMATIONS = 1000.0
        private const val TOTAL_SIMPLE_OBJECTS = 15
        private const val TOTAL_COMPLEX_OBJECTS = 5
        private const val SIMPLE_OBJECTS_MAX_TRANSFORMATION_TIME = 0.2
        private const val COMPLEX_OBJECTS_MAX_TRANSFORMATION_TIME = 1.0
        private const val WAIT_INTERVAL = 1000
        private const val NO_WAIT_INTERVAL = 0
        private const val ACTIVE = true
        private var fromFoo: FromFoo? = null
        private var fromFooSimple: FromFooSimple? = null
        private var fromSubFooList: MutableList<FromSubFoo?>? = null
        private var sourceFooSimpleList: MutableList<String>? = null
        private var fromSubFoo: FromSubFoo? = null
        private var fromFooSubClass: FromFooSubClass? = null
    }
}
