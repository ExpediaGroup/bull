/**
 * Copyright (C) 2019-2020 Expedia, Inc.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hotels.beans.generator.core.mapping;

import java.util.Objects;

import org.apache.commons.lang3.NotImplementedException;

import com.hotels.transformer.constant.ClassType;
import com.hotels.transformer.utils.ClassUtils;
import com.squareup.javapoet.CodeBlock;

/**
 * This is the base class for modeling the code block that maps a source object to a destination object.
 * @author mmirk
 */
public abstract class MappingCode {
    /**
     * A reusable instance of ClassUtils.
     */
    protected static final ClassUtils CUTILS = new ClassUtils();

    /**
     * Obtains an instance of {@code MappingCode} from a source and destination.
     * <p>
     * The returned instance is specialized to create a {@link CodeBlock} for mapping
     * an object of a source type to a destination type.
     * @param source the source type
     * @param destination the destination type
     * @return the mapping code from source to destination
     */
    public static MappingCode of(final Class<?> source, final Class<?> destination) {
        Objects.requireNonNull(source, "source type can't be null");
        Objects.requireNonNull(destination, "destination type can't be null");

        final ClassType classType = CUTILS.getClassType(destination);
        switch (classType) {
            case MUTABLE:
                return new JavaBeanCode(source, destination);
            case IMMUTABLE:
                throw new NotImplementedException("Transformation code for type IMMUTABLE is not supported");
            case MIXED:
                throw new NotImplementedException("Transformation code for type MIXED is not supported");
            default:
                throw new IllegalStateException("Unsupported destination type: " + classType);
        }
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
}
