package com.hotels.beans.sample.builder;

import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;

@Getter
@Setter
public class ToFoo {
    private String name;
    private BigInteger id;

    private ToFoo() {}

    // getters

    public static class Builder {
        private String name;
        private BigInteger id;

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public Builder withId(BigInteger id) {
            this.id = id;
            return this;
        }

        public ToFoo build() {
            ToFoo toFoo = new ToFoo();
            toFoo.id = this.id;
            toFoo.name = this.name;
            return toFoo;
        }
    }
}