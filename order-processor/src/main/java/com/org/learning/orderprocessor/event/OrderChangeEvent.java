package com.org.learning.orderprocessor.event;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Debezium change event envelope for the orders table
 * (schemas disabled on the connector, so the message payload is the envelope itself).
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record OrderChangeEvent(
        OrderSnapshot before,
        OrderSnapshot after,
        Operation op,
        @JsonProperty("ts_ms") Long tsMs,
        Source source) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Source(
            String db,
            String schema,
            String table,
            String lsn,
            @JsonProperty("ts_ms") Long tsMs) {
    }
}
