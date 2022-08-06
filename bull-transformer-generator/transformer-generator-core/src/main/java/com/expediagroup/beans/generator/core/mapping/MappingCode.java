/**
 * Copyright (C) 2019-2021 Expedia, Inc.
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
package com.expediagroup.beans.generator.core.mapping;

import com.expediagroup.transformer.utils.ClassUtils;
import com.squareup.javapoet.CodeBlock;

/**
 * This is the base class for modeling the code block that maps a source object to a destination object.
 * Subclasses must provide an implementation of {@code creation()} and {@code initialization()} methods,
 * to create mapping code specialized for different kinds of source and destination.
 */
public abstract class MappingCode {
    /**
     * A reusable instance of ClassUtils for introspecting types.
     */
    private final ClassUtils classUtils;

    /**
     * Instantiates a new Mapping code with the given ClassUtils.
     * @param clsUtils an utility for introspecting types
     */
    MappingCode(final ClassUtils clsUtils) {
        this.classUtils = clsUtils;
    }

    /**
     * Build the mapping code block.
     * @return the code block
     */
    public final CodeBlock build() {
        return CodeBlock.builder()
                .addStatement(creation())
                .add(initialization())
                .addStatement(returning())
                .build();
    }

    /**
     * Creation code statement, models the instantiation of the destination type.
     * Implementing classes must know how to create an instance of the destination type.
     * By convention the instance identifier must be "destination".
     * @return the code block
     */
    protected abstract CodeBlock creation();

    /**
     * Initialization code block, models the mapping between the source and destination types.
     * Implementing classes must know  which properties to map, how to access values from both types,
     * and how to apply transformations.
     * By convention the instance identifiers must be "source" and "destination".
     * @return the code block
     */
    protected abstract CodeBlock initialization();

    /**
     * Returning code statement, models the returning of the destination instance.
     * Implementing classes usually do not need to override this method, unless they have a particular reason.
     * By convention the instance identifier must be "destination".
     * @return the code block
     */
    protected CodeBlock returning() {
        return CodeBlock.of("return destination");
    }

    /**
     * Gets class utils instance.
     * @return the class utils
     */
    protected final ClassUtils getClassUtils() {
        return classUtils;
    }
}
