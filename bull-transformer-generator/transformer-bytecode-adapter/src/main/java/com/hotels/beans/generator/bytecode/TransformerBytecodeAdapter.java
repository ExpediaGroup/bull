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

package com.hotels.beans.generator.bytecode;

import java.io.IOException;
import java.io.StringWriter;
import java.io.UncheckedIOException;

import com.hotels.beans.generator.core.Transformer;
import com.hotels.beans.generator.core.TransformerSpec;
import com.squareup.javapoet.JavaFile;

import lombok.Builder;

import net.openhft.compiler.CachedCompiler;
import net.openhft.compiler.CompilerUtils;

/**
 * TODO: Document me.
 */
@Builder
public final class TransformerBytecodeAdapter {

    /**
     * TODO
     */
    static final String DEFAULT_PACKAGE = "com.hotels.beans.transformer";

    /**
     * TODO: Document me.
     */
    private final TransformerSpec spec;

    /**
     * TODO: Document me.
     */
    @Builder.Default
    private final String packageName = DEFAULT_PACKAGE;

    @Builder.Default
    private final CachedCompiler compiler = CompilerUtils.CACHED_COMPILER;

    /**
     * TODO: Document me.
     * @param <A> the type parameter
     * @param <B> the type parameter
     * @param source the source
     * @param destination the destination
     * @return the transformer
     */
    @SuppressWarnings("Unchecked Assignment")
    public <A, B> Transformer<A, B> newTransformer(final Class<A> source, final Class<B> destination) {
        JavaFile javaFile = JavaFile.builder(packageName, spec.build(source, destination)).build();
        String transformerCode = sourceCode(javaFile);
        String className = qualifiedName(javaFile);
        try {
            Class<Transformer<A, B>> transformerClass = compiler.loadFromJava(className, transformerCode);
            return transformerClass.getDeclaredConstructor().newInstance();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(
                    "There was an error loading the compiled transformer '" + className + "'", e);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(
                    "There was an error instantiating the transformer '" + className + "': "
                            + "check that the TransformerSpec used by this adapter "
                            + "generates a public no-args constructor "
                            + "and that it doesn't throw", e);
        }
    }

    /**
     * Returns a {@code String} containing the full source code of {@code javaFile},
     * including package and import declarations.
     * @param javaFile a Java file containing a class
     * @return the full source code of the Java file
     */
    private String sourceCode(final JavaFile javaFile) {
        StringWriter codeBuffer = new StringWriter();
        try {
            javaFile.writeTo(codeBuffer);
        } catch (IOException e) {
            throw new UncheckedIOException("Unable to write code to in-memory buffer", e);
        }
        return codeBuffer.toString();
    }

    /**
     * Returns the fully qualified name of the class inside {@code javaFile}, for example {@code "com.foo.bar.Type"}.
     * @param javaFile a Java file containing a class
     * @return the fully qualified name of the class
     */
    private static String qualifiedName(final JavaFile javaFile) {
        return javaFile.packageName + "." + javaFile.typeSpec.name;
    }
}
