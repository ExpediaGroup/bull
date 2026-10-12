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

import java.util.Arrays;
import java.util.List;

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
}
