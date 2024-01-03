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
package com.expediagroup.beans.generator.bytecode;

import java.io.IOException;
import java.util.Map;

import com.expediagroup.beans.generator.core.Transformer;
import com.expediagroup.beans.generator.core.TransformerSpec;
import com.expediagroup.beans.generator.core.error.TransformerGeneratorException;
import com.itranswarp.compiler.JavaStringCompiler;
import com.squareup.javapoet.JavaFile;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

/**
 * A bytecode adapter for {@link TransformerSpec} that compiles at runtime the source code
 * represented by the model, and creates a new instance of {@link Transformer}.
 *
 * This component is intended for internal usage in Bull as it's a low-level wrapper for
 * runtime compilation of models. Clients should prefer higher level interfaces
 * such as {@code TransformerRegistry}.
 */
@Builder
@Slf4j
public final class TransformerBytecodeAdapter {

    /**
     * The default Transformer package if none is specified.
     */
    static final String DEFAULT_PACKAGE = "com.expediagroup.beans.transformer";

    /**
     * The Transformer model for generating source code.
     */
    private final TransformerSpec spec;

    /**
     * The Java package to use for the generated transformer classes.
     */
    @Builder.Default
    private final String packageName = DEFAULT_PACKAGE;

    /**
     * The compiler instance to use at runtime.
     */
    @Builder.Default
    private final JavaStringCompiler compiler = new JavaStringCompiler();

    /**
     * Dynamically create a new {@link Transformer} instance from {@code source} to {@code destination}.
     * @param <A> the source type
     * @param <B> the destination type
     * @param source the source class
     * @param destination the destination class
     * @return a new Transformer instance
     */
    @SuppressWarnings("unchecked")
    public <A, B> Transformer<A, B> newTransformer(final Class<A> source, final Class<B> destination) {
        JavaFile javaFile = JavaFile.builder(packageName, spec.build(source, destination)).build();
        String transformerCode = javaFile.toString();
        String className = qualifiedName(javaFile);
        try {
            log.info("Compiling and loading '{}'", className);
            log.debug("\n{}", transformerCode);
            Map<String, byte[]> results = compiler.compile(javaFile.typeSpec.name + ".java", transformerCode);
            Class<Transformer<A, B>> transformerClass = (Class<Transformer<A, B>>) compiler.loadClass(className, results);
            return transformerClass.getDeclaredConstructor().newInstance();
        } catch (IOException e) {
            throw new TransformerGeneratorException(
                    "There was an error compiling '" + className + "'", e);
        } catch (ClassNotFoundException e) {
            throw new TransformerGeneratorException(
                    "There was an error loading the compiled transformer '" + className + "'", e);
        } catch (ReflectiveOperationException e) {
            throw new TransformerGeneratorException(
                    "There was an error instantiating the transformer '" + className + "': "
                            + "check that the TransformerSpec used by this adapter "
                            + "generates a public no-args constructor "
                            + "and that it doesn't throw", e);
        }
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
