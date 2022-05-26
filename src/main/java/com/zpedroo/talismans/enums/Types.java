package com.zpedroo.talismans.enums;

public enum Types {
    LUCK,
    MULTIPLIER,
    MINING;

    public static Types getByName(String name) {
        for (Types types : values()) {
            if (types.name().equals(name)) return types;
        }

        return null;
    }
}