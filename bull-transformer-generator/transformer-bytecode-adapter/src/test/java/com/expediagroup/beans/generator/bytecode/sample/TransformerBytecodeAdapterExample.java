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
package com.expediagroup.beans.generator.bytecode.sample;

import static org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString;

import static lombok.AccessLevel.PRIVATE;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.expediagroup.beans.generator.bytecode.TransformerBytecodeAdapter;
import com.expediagroup.beans.generator.core.Transformer;
import com.expediagroup.beans.generator.core.TransformerSpec;
import com.expediagroup.beans.generator.core.mapping.MappingCodeFactory;
import com.expediagroup.beans.generator.core.sample.javabean.Destination;
import com.expediagroup.beans.generator.core.sample.javabean.Source;

import lombok.NoArgsConstructor;

/**
 * Example of generating and using a Transformer at runtime.
 */
@NoArgsConstructor(access = PRIVATE)
public final class TransformerBytecodeAdapterExample {
    private static Destination getExpectedResult() {
        Destination destination = new Destination();
        destination.setABoolean(true);
        destination.setAString("hello");
        return destination;
    }

    /**
     * Entry point for manual testing.
     * Run this to compare the usage of a generated transformer with the above reference result.
     * @param args the args
     */
    public static void main(final String[] args) {
        // create a transformer model
        TransformerSpec spec = new TransformerSpec(MappingCodeFactory.newInstance());
        // create a bytecode adapter for the model
        TransformerBytecodeAdapter bytecode = TransformerBytecodeAdapter.builder()
                .spec(spec)
                .build();
        // generate and compile a new Transformer
        Transformer<Source, Destination> transformer = bytecode.newTransformer(Source.class, Destination.class);

        final int anInt = 42;
        Source source = new Source();
        source.setABoolean(true);
        source.setAnInt(anInt);
        source.setAString("hello");
        // use the generated Transformer
        Destination destination = transformer.transform(source);

        ToStringBuilder.setDefaultStyle(ToStringStyle.SHORT_PREFIX_STYLE);
        System.out.printf("Transformed: %s%n", reflectionToString(destination));
        System.out.printf("Expected: %s%n", reflectionToString(getExpectedResult()));
    }
}
