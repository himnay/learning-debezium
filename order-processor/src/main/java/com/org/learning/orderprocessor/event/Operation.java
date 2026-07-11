package com.org.learning.orderprocessor.event;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Debezium change event operation codes.
 */
public enum Operation {
    CREATE("c"),
    UPDATE("u"),
    DELETE("d"),
    READ("r");

    private final String code;

    Operation(String code) {
        this.code = code;
    }

    @JsonValue
    public String getCode() {
        return code;
    }
}
