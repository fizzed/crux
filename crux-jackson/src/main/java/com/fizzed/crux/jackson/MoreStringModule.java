package com.fizzed.crux.jackson;

import com.fasterxml.jackson.databind.module.SimpleModule;

public class MoreStringModule extends SimpleModule {

    public MoreStringModule(
            boolean trim,
            boolean emptyToNull) {

        this.addDeserializer(String.class, new MoreStringDeserializer(
            trim, emptyToNull));
    }

}