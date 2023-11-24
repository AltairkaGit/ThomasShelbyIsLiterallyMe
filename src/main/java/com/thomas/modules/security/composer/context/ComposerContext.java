package com.thomas.modules.security.composer.context;

import java.util.EnumMap;

public class ComposerContext {
    private final EnumMap<ComposerContextEnum, Object> map;

    public ComposerContext() {
        map = new EnumMap<>(ComposerContextEnum.class);
    }

    public <V> void put(ComposerContextEnum key, V value) {
        map.put(key, value);
    }

    @SuppressWarnings("unchecked")
    public <V> V get(ComposerContextEnum key) {
        Object value = map.get(key);
        return (V) value;
    }
}
