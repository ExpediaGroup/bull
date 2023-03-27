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
package com.expediagroup.beans.generator.core.sample.javabean;

import com.expediagroup.beans.generator.core.Transformer;
import com.expediagroup.beans.generator.core.TransformerSpec;
import com.expediagroup.beans.generator.core.mapping.MappingCodeFactory;

/**
 * Example used as reference for the generated code.
 */
@SuppressWarnings("unused")
public final class TransformerExample implements Transformer<Source, Destination> {
    @Override
    public Destination transform(final Source source) { // 'final' required by Checkstyle, not part of the generated code
        Destination destination = new Destination();
        destination.setABoolean(source.isABoolean());
        destination.setAString(source.getAString());
        return destination;
    }

    /**
     * Entry point for manual testing.
     * Run this to compare transformer serialization with the above reference code.
     * @param args the args
     */
    public static void main(final String[] args) {
        System.out.println(new TransformerSpec(MappingCodeFactory.newInstance())
                .build(Source.class, Destination.class));
    }
}
