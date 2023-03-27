/**
 * Copyright (C) 2018-2023 Expedia, Inc.
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

import static com.expediagroup.transformer.validator.Validator.notNull;

import static lombok.AccessLevel.PRIVATE;

import org.apache.commons.lang3.NotImplementedException;

import com.expediagroup.transformer.constant.ClassType;
import com.expediagroup.transformer.utils.ClassUtils;
import com.squareup.javapoet.CodeBlock;

import lombok.RequiredArgsConstructor;

/**
 * A factory to obtain an instance of {@link MappingCode}.
 */
@RequiredArgsConstructor(access = PRIVATE)
public final class MappingCodeFactory {
    /**
     * An instance of ClassUtils.
     */
    private final ClassUtils classUtils;

    /**
     * Gets an instance of {@code MappingCodeFactory}.
     * @return a MappingCode instance
     */
    public static MappingCodeFactory newInstance() {
        return newInstance(new ClassUtils());
    }

    /**
     * Gets an instance of {@code MappingCodeFactory} with the provided classUtils.
     * @param classUtils the class utils
     * @return a MappingCode instance
     */
    public static MappingCodeFactory newInstance(final ClassUtils classUtils) {
        return new MappingCodeFactory(classUtils);
    }

    /**
     * Obtains an instance of {@code MappingCode} from a source and destination.
     * The returned instance is specialized to create a {@link CodeBlock} for mapping
     * an object of a source type to a destination type.
     * @param source the source type
     * @param destination the destination type
     * @return the mapping code from source to destination
     */
    public MappingCode of(final Class<?> source, final Class<?> destination) {
        notNull(source, "source type can't be null");
        notNull(destination, "destination type can't be null");
        ClassType classType = classUtils.getClassType(destination);
        switch (classType) {
            case MUTABLE:
                return new JavaBeanCode(source, destination, classUtils);
            case IMMUTABLE:
                throw new NotImplementedException("Transformation code for type IMMUTABLE is not supported");
            case MIXED:
                throw new NotImplementedException("Transformation code for type MIXED is not supported");
            default:
                throw new AssertionError("Unable to determine MappingCode implementation: "
                        + "destination class type '" + classType + "' is unsupported. "
                        + "Please modify this method to handle the case or check the implementation of "
                        + "com.expediagroup.transformer.utils.ClassUtils.getClassType");
        }
    }
}
