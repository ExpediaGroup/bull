/**
 * Copyright (C) 2019-2020 Expedia, Inc.
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

package com.hotels.transformer.error;

/**
 * Invalid transformation function exception.
 */
public class InvalidFunctionException extends RuntimeException {
    /**
     * Constructs a new invalid function transformer exception with the specified detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     * @param message the detail message. The detail message is saved for later
     *                retrieval by the {@link #getMessage()} method.
     * @param cause the given transformation function is not valid for the given object.
     */
    public InvalidFunctionException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
