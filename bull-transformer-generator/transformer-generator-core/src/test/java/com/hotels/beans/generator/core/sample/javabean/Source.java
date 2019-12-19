package com.hotels.beans.generator.core.sample.javabean;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Sample class that represents a transformer's source type.
 *
 * @author mmirk
 */
@Getter
@Setter
public class Source {
    int anInt;
    String aString;
    boolean aBoolean;
    List<Long> longList;
}
