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

package com.hotels.beans.generator.source.sample;

import static lombok.AccessLevel.PRIVATE;

import java.io.IOException;
import java.nio.file.Path;

import com.hotels.beans.generator.core.TransformerSpec;
import com.hotels.beans.generator.core.mapping.MappingCodeFactory;
import com.hotels.beans.generator.core.sample.javabean.Destination;
import com.hotels.beans.generator.core.sample.javabean.Source;
import com.hotels.beans.generator.source.TransformerFile;
import com.hotels.beans.generator.source.TransformerSourceAdapter;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Example of generating and using a Transformer at runtime.
 */
@Slf4j
@NoArgsConstructor(access = PRIVATE)
public final class TransformerSourceAdapterExample {
    /**
     * Entry point for manual testing.
     * Run this to compare the usage of a generated transformer with the above reference result.
     * @param args the args
     */
    public static void main(final String[] args) {
        // create a transformer model
        TransformerSpec spec = new TransformerSpec(MappingCodeFactory.newInstance());
        // create a bytecode adapter for the model
        // with default destination path and Java package
        TransformerSourceAdapter source = TransformerSourceAdapter.builder()
                .spec(spec)
                .build();
        // generate a new Transformer source file
        TransformerFile transformerFile = source.newTransformerFile(Source.class, Destination.class);
        // write file to disk
        try {
            Path sourcePath = transformerFile.write();
            log.info("find generated source at: {}", sourcePath.toAbsolutePath());
        } catch (IOException e) {
            log.error("could not write source: {}", e.getMessage(), e);
        }
    }
}
