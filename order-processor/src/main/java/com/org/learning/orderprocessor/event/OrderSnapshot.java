package com.org.learning.orderprocessor.event;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

/**
 * Row image of the orders table as emitted by Debezium (before/after).
 * Timestamps arrive as ISO-8601 strings (io.debezium.time.ZonedTimestamp),
 * price as a string because the connector uses decimal.handling.mode=string.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record OrderSnapshot(
        Long id,
        @JsonProperty("customer_id") String customerId,
        String product,
        Integer quantity,
        BigDecimal price,
        String status,
        @JsonProperty("created_at") OffsetDateTime createdAt,
        @JsonProperty("updated_at") OffsetDateTime updatedAt) {
}
