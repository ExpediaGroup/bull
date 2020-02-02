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

package com.hotels.transformer.annotation;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Annotation for the auto injection of immutable beans.
 * As with Java up to version 8 is not possible to retrieve the constructor's parameter name through reflection,
 * this annotation needs to be specified next to each one of them.
 * e.g.
 * {@code
 *      public class Square {
 *          private final int x;
 *          private final int y;
 *
 *          public Square(@ConstructorArg("x") final int x, @ConstructorArg("y") final int y) {
 *              this.x = x;
 *              this.y = y;
 *          }
 *      }
 * }
 */
@Retention(RUNTIME)
@Target(PARAMETER)
public @interface ConstructorArg {
    /**
     * The field name in the target object. It's needed because the field name cannot be retrieved through reflection in the constructor.
     * @return the field name
     */
    String value();
}
