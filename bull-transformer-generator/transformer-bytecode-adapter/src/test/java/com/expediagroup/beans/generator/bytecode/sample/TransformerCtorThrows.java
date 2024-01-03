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

import com.expediagroup.beans.generator.core.Transformer;
import com.expediagroup.beans.generator.core.sample.javabean.Destination;
import com.expediagroup.beans.generator.core.sample.javabean.Source;

/**
 * Non-compliant transformer implementation: constructor throws exception.
 */
public final class TransformerCtorThrows implements Transformer<Source, Destination> {
    TransformerCtorThrows() {
        throw new RuntimeException("simulated error");
    }

    @Override
    public Destination transform(final Source source) {
        return null;
    }
}
