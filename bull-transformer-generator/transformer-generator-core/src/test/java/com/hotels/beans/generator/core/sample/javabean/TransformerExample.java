package com.hotels.beans.generator.core.sample.javabean;

import com.hotels.beans.generator.core.Transformer;

/**
 * Example used as reference for the generated code.
 *
 * @author mmirk
 */
@SuppressWarnings("unused")
public class TransformerExample implements Transformer<Source, Destination> {
    @Override
    public Destination transform(Source source) {
        Destination destination = new Destination();
        destination.setABoolean(source.isABoolean());
        destination.setAString(source.getAString());
        return destination;
    }
}
