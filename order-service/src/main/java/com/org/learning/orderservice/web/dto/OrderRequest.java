package com.org.learning.orderservice.web.dto;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

/**
 * Limits mirror the orders table (V1__create_orders_table.sql): without them an over-long string
 * fails as a 500 at insert time, and NUMERIC(10,2) silently rounds a price like 9.999 to 10.00
 * while the response still echoes 9.999.
 */
public record OrderRequest(
        @NotBlank @Size(max = 64) String customerId,
        @NotBlank @Size(max = 255) String product,
        @NotNull @Positive Integer quantity,
        @NotNull @Positive @Digits(integer = 8, fraction = 2) BigDecimal price) {
}
