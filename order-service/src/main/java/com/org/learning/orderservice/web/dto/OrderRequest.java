package com.org.learning.orderservice.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record OrderRequest(
        @NotBlank String customerId,
        @NotBlank String product,
        @NotNull @Positive Integer quantity,
        @NotNull @Positive BigDecimal price) {
}
