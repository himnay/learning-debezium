package com.org.learning.orderservice.web.dto;

import com.org.learning.orderservice.domain.OrderStatus;
import jakarta.validation.constraints.NotNull;

public record OrderStatusUpdateRequest(@NotNull OrderStatus status) {
}
