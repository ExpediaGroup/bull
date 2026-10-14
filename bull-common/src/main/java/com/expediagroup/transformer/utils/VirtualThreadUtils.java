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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Function;

/**
 * Utility class for leveraging Java 19+ Virtual Threads for efficient concurrent operations.
 * Virtual threads provide lightweight concurrency with improved throughput for I/O-bound tasks.
 */
public final class VirtualThreadUtils {
    /**
     * Executor service using virtual threads for concurrent transformations.
     * Virtual threads have minimal overhead and are suitable for high-concurrency scenarios.
     */
    private static final ExecutorService VIRTUAL_THREAD_EXECUTOR = Executors.newVirtualThreadPerTaskExecutor();
    /**
     * Error message used when a concurrent transformation fails.
     */
    private static final String TRANSFORMATION_ERROR = "Error during concurrent transformation";

    /**
     * Private constructor to prevent instantiation.
     */
    private VirtualThreadUtils() {
    }

    /**
     * Transforms a collection of items concurrently using virtual threads.
     * Useful for transforming large collections with I/O-bound operations.
     * @param items the items to transform
     * @param transformer the transformation function
     * @param <T> the source type
     * @param <R> the result type
     * @return list of transformed items in the same order
     */
    public static <T, R> List<R> transformConcurrently(final Collection<T> items, final Function<T, R> transformer) {
        final var futures = items.stream()
                .map(item -> VIRTUAL_THREAD_EXECUTOR.submit(() -> transformer.apply(item)))
                .toList();
        final var results = new ArrayList<R>(items.size());
        try {
            for (var future : futures) {
                results.add(future.get());
            }
            return results;
        } catch (InterruptedException e) {
            futures.forEach(future -> future.cancel(true));
            Thread.currentThread().interrupt();
            throw new IllegalStateException(TRANSFORMATION_ERROR, e);
        } catch (ExecutionException e) {
            futures.forEach(future -> future.cancel(true));
            throw new IllegalStateException(TRANSFORMATION_ERROR, e);
        }
    }

    /**
     * Executes a task asynchronously on a virtual thread.
     * @param task the task to execute
     * @param <R> the result type
     * @return a Future representing the pending result
     */
    public static <R> Future<R> executeAsync(final java.util.concurrent.Callable<R> task) {
        return VIRTUAL_THREAD_EXECUTOR.submit(task);
    }

    /**
     * Creates a virtual thread with a custom name for better debugging.
     * @param name the thread name
     * @param runnable the task to run
     * @return the created virtual thread
     */
    public static Thread createVirtualThread(final String name, final Runnable runnable) {
        return Thread.ofVirtual()
                .name(name)
                .unstarted(runnable);
    }
}
