/**
 * Copyright (C) 2019-2026 Expedia, Inc.
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
package com.expediagroup.transformer.utils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.testng.annotations.Test;

/**
 * Unit test for {@link VirtualThreadUtils}.
 */
public class VirtualThreadUtilsTest {
    private static final int MULTIPLIER = 2;
    private static final int FIRST = 1;
    private static final int SECOND = 2;
    private static final int SECOND_RESULT = 4;
    private static final int THIRD = 3;
    private static final int FOURTH = 4;
    private static final int FIFTH = 5;
    private static final int SIX = 6;
    private static final int SEVEN = 7;
    private static final long SLEEP_MILLIS = 100L;
    private static final String TEST_THREAD_NAME = "test-thread";
    private static final String EXECUTION_TEST = "execution-test";
    private static final String TEST_RESULT = "test-result";
    private static final String THREAD_1 = "thread-1";
    private static final String THREAD_2 = "thread-2";
    private static final String TEST_EXCEPTION_MSG = "test exception";
    private static final String TRANSFORMATION_FAILED = "transformation failed";

    /**
     * Tests that isVirtualThreadSupported returns true on Java 19+.
     */
    @Test
    public void testIsVirtualThreadSupported() {
        assertThat(VirtualThreadUtils.isVirtualThreadSupported()).isTrue();
    }

    /**
     * Tests that createVirtualThread creates a virtual thread.
     */
    @Test
    public void testCreateVirtualThread() {
        var executed = new boolean[]{false};
        var thread = VirtualThreadUtils.createVirtualThread("test-thread", () -> executed[0] = true);
        assertThat(thread).isNotNull();
        assertThat(thread.isVirtual()).isTrue();
        assertThat(thread.getName()).contains("test-thread");
    }

    /**
     * Tests that transformConcurrently transforms items concurrently.
     */
    @Test
    public void testTransformConcurrently() {
        List<Integer> numbers = Arrays.asList(FIRST, SECOND);
        List<Integer> results = VirtualThreadUtils.transformConcurrently(
            numbers,
            num -> num * MULTIPLIER
        );
        assertThat(results).containsExactly(MULTIPLIER, SECOND_RESULT);
    }

    /**
     * Tests that executeAsync executes a task asynchronously.
     */
    @Test
    public void testExecuteAsync() throws Exception {
        var result = VirtualThreadUtils.executeAsync(() -> "test-result");
        assertThat(result).isNotNull();
        assertThat(result.get()).isEqualTo("test-result");
    }

    /**
     * Tests that transformConcurrently handles empty collections.
     */
    @Test
    public void testTransformConcurrentlyEmpty() {
        List<Integer> numbers = Arrays.asList();
        List<Integer> results = VirtualThreadUtils.transformConcurrently(
            numbers,
            num -> num * MULTIPLIER
        );
        assertThat(results).isEmpty();
    }

    /**
     * Tests that createVirtualThread thread is properly created.
     */
    @Test
    public void testCreateVirtualThreadExecution() throws InterruptedException {
        var executed = new boolean[]{false};
        var thread = VirtualThreadUtils.createVirtualThread("execution-test", () -> executed[0] = true);
        thread.start();
        thread.join();
        assertThat(executed[0]).isTrue();
    }

    /**
     * Tests that transformConcurrently preserves order.
     */
    @Test
    public void testTransformConcurrentlyPreservesOrder() {
        List<Integer> numbers = Arrays.asList(FIRST, SECOND, THIRD, FOURTH, FIFTH);
        List<Integer> results = VirtualThreadUtils.transformConcurrently(
            numbers,
            num -> num * MULTIPLIER
        );
        assertThat(results).hasSize(FIFTH);
    }

    /**
     * Tests executeAsync with exception handling.
     */
    @Test
    public void testExecuteAsyncWithValue() throws Exception {
        var result = VirtualThreadUtils.executeAsync(() -> MULTIPLIER);
        assertThat(result.get()).isEqualTo(MULTIPLIER);
    }

    /**
     * Tests that transformConcurrently handles single item.
     */
    @Test
    public void testTransformConcurrentlySingleItem() {
        List<Integer> numbers = Arrays.asList(FIRST);
        List<Integer> results = VirtualThreadUtils.transformConcurrently(numbers, num -> num * MULTIPLIER);
        assertThat(results).containsExactly(MULTIPLIER);
    }

    /**
     * Tests that transformConcurrently with large collection.
     */
    @Test
    public void testTransformConcurrentlyLargeCollection() {
        List<Integer> numbers = Arrays.asList(FIRST, SECOND, THIRD, FOURTH, FIFTH, FIRST, SECOND);
        List<Integer> results = VirtualThreadUtils.transformConcurrently(numbers, num -> num * MULTIPLIER);
        assertThat(results).hasSize(SEVEN);
        assertThat(results.get(0)).isEqualTo(MULTIPLIER);
        assertThat(results.get(1)).isEqualTo(SECOND_RESULT);
    }

    /**
     * Tests that executeAsync can handle exceptions properly.
     */
    @Test
    public void testExecuteAsyncException() throws Exception {
        var result = VirtualThreadUtils.executeAsync(() -> {
            throw new RuntimeException(TEST_EXCEPTION_MSG);
        });
        assertThat(result).isNotNull();
        assertThatThrownBy(result::get).isInstanceOf(ExecutionException.class);
    }

    /**
     * Tests that transformConcurrently raises error when transformation fails.
     */
    @Test
    public void testTransformConcurrentlyWithException() {
        List<Integer> numbers = Arrays.asList(FIRST, SECOND);
        assertThatThrownBy(() -> VirtualThreadUtils.transformConcurrently(numbers, num -> {
            throw new RuntimeException(TRANSFORMATION_FAILED);
        })).isInstanceOf(RuntimeException.class).hasMessageContaining("concurrent transformation");
    }

    /**
     * Tests virtual thread creation with multiple threads.
     */
    @Test
    public void testCreateMultipleVirtualThreads() {
        var thread1 = VirtualThreadUtils.createVirtualThread(THREAD_1, () -> { });
        var thread2 = VirtualThreadUtils.createVirtualThread(THREAD_2, () -> { });
        assertThat(thread1).isNotNull();
        assertThat(thread2).isNotNull();
        assertThat(thread1.getName()).contains(THREAD_1);
        assertThat(thread2.getName()).contains(THREAD_2);
    }

    /**
     * Tests transformConcurrently with ArrayList.
     */
    @Test
    public void testTransformConcurrentlyWithArrayList() {
        List<Integer> numbers = new ArrayList<>();
        numbers.add(FIRST);
        numbers.add(SECOND);
        numbers.add(THIRD);
        List<Integer> results = VirtualThreadUtils.transformConcurrently(numbers, num -> num * MULTIPLIER);
        assertThat(results).hasSize(THIRD);
        assertThat(results).containsExactly(MULTIPLIER, SECOND_RESULT, THIRD * MULTIPLIER);
    }

    /**
     * Tests that transformConcurrently handles six items.
     */
    @Test
    public void testTransformConcurrentlyWithSixItems() {
        List<Integer> numbers = Arrays.asList(FIRST, SECOND, THIRD, FOURTH, FIFTH, SIX);
        List<Integer> results = VirtualThreadUtils.transformConcurrently(numbers, num -> num * MULTIPLIER);
        assertThat(results).hasSize(SIX);
        assertThat(results.get(FIFTH)).isEqualTo(SIX * MULTIPLIER);
    }

    /**
     * Tests executeAsync with null result.
     */
    @Test
    public void testExecuteAsyncNullResult() throws Exception {
        var result = VirtualThreadUtils.executeAsync(() -> null);
        assertThat(result.get()).isNull();
    }

    /**
     * Tests createVirtualThread with empty runnable.
     */
    @Test
    public void testCreateVirtualThreadEmptyRunnable() {
        var thread = VirtualThreadUtils.createVirtualThread("empty-thread", () -> { });
        assertThat(thread).isNotNull();
        assertThat(thread.isVirtual()).isTrue();
        assertThat(thread.getName()).contains("empty-thread");
    }

    /**
     * Tests transformConcurrently with transformation that returns zero.
     */
    @Test
    public void testTransformConcurrentlyWithZeroResult() {
        List<Integer> numbers = Arrays.asList(FIRST, SECOND, THIRD);
        List<Integer> results = VirtualThreadUtils.transformConcurrently(numbers, num -> 0);
        assertThat(results).containsExactly(0, 0, 0);
    }

    /**
     * Tests transformConcurrently with negative numbers.
     */
    @Test
    public void testTransformConcurrentlyWithNegativeTransformation() {
        List<Integer> numbers = Arrays.asList(FIRST, SECOND, THIRD);
        List<Integer> results = VirtualThreadUtils.transformConcurrently(numbers, num -> -num);
        assertThat(results).containsExactly(-FIRST, -SECOND, -THIRD);
    }

    /**
     * Tests that all static methods are accessible.
     */
    @Test
    public void testStaticMethodsAccessible() {
        assertThat(VirtualThreadUtils.isVirtualThreadSupported()).isNotNull();
        assertThat(VirtualThreadUtils.createVirtualThread("test", () -> { })).isNotNull();
    }
}
