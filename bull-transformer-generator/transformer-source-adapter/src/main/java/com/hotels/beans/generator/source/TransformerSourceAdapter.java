package com.hotels.beans.generator.source;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.hotels.beans.generator.core.Transformer;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;

import lombok.Builder;

/**
 * TODO: Document me
 */
@Builder
public class TransformerSourceAdapter {
    /**
     * The default source path if none is specified.
     */
    static final Path DEFAULT_PATH = Paths.get("generated-sources/bull");

    /**
     * The default transformer package if none is specified.
     */
    static final String DEFAULT_PACKAGE = Transformer.class.getPackageName();

    @Builder.Default
    private final Path basePath = DEFAULT_PATH;

    @Builder.Default
    private final String packageName = DEFAULT_PACKAGE;

    private final SourceTransformerSpec spec;

    public <A, B> TransformerFile newTransformerFile(final Class<A> source, final Class<B> destination) {
        return new TransformerFile(spec.build(source, destination));
    }

    public class TransformerFile {
        private final TypeSpec transformer;

        private TransformerFile(final TypeSpec transformer) {
            this.transformer = transformer;
        }

        public Path write() throws IOException {
            return JavaFile.builder(packageName, transformer)
                    .skipJavaLangImports(true)
                    .build()
                    .writeToPath(basePath);
        }
    }
}
