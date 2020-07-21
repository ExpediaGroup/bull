package com.hotels.beans.generator.source;

import java.time.Clock;

import javax.annotation.processing.Generated;

import com.hotels.beans.generator.core.Transformer;
import com.hotels.beans.generator.core.TransformerSpec;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.TypeSpec;


/**
 * A decorator of {@link TransformerSpec} which adds useful metadata to the transformer model.
 * It's used to generate readable source files.
 */
public class SourceTransformerSpec {
    /**
     * The transfomer model to decorate for producing a source file.
     */
    private final TransformerSpec spec;

    /**
     * The clock to use for generating source timestamps.
     */
    private final Clock clock;

    public SourceTransformerSpec(final TransformerSpec transformerSpec) {
        this(transformerSpec, Clock.systemUTC());
    }

    public SourceTransformerSpec(final TransformerSpec transformerSpec, final Clock clock) {
        this.spec = transformerSpec;
        this.clock = clock;
    }

    public <A, B> TypeSpec build(final Class<A> source, final Class<B> destination) {
        return spec.build(source, destination)
                .toBuilder()
                .addJavadoc(docFor(source, destination))
                .addAnnotation(AnnotationSpec.builder(Generated.class)
                        .addMember("value", getClass().getName())
                        .addMember("date", clock.instant().toString())
                        .build())
                .build();
    }

    /**
     * Creates a javadoc for the transformer implementation that includes {@code source} and {@code destination}.
     * @param source the source type
     * @param destination the destination type
     * @return a JavaDoc comment
     */
    private CodeBlock docFor(final Class<?> source, final Class<?> destination) {
        return CodeBlock.of("A {@link $T} that can map an instance of $T to a new instance of $T.",
                Transformer.class, source, destination);
    }

}
