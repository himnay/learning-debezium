package com.org.learning.orderservice.service;

import com.org.learning.orderservice.domain.Order;
import com.org.learning.orderservice.domain.OrderStatus;
import com.org.learning.orderservice.exception.OrderNotFoundException;
import com.org.learning.orderservice.repository.OrderRepository;
import com.org.learning.orderservice.web.dto.OrderRequest;
import com.org.learning.orderservice.web.dto.OrderResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;

    @Transactional
    public OrderResponse create(OrderRequest request) {
        var order = Order.builder()
                .customerId(request.customerId())
                .product(request.product())
                .quantity(request.quantity())
                .price(request.price())
                .status(OrderStatus.NEW)
                .build();
        var saved = orderRepository.save(order);
        log.info("Created order {}", saved.getId());
        return OrderResponse.from(saved);
    }

    @Transactional(readOnly = true)
    public OrderResponse get(Long id) {
        return OrderResponse.from(findOrder(id));
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> list() {
        return orderRepository.findAll().stream()
                .map(OrderResponse::from)
                .toList();
    }

    @Transactional
    public OrderResponse update(Long id, OrderRequest request) {
        var order = findOrder(id);
        order.setCustomerId(request.customerId());
        order.setProduct(request.product());
        order.setQuantity(request.quantity());
        order.setPrice(request.price());
        // flush so @PreUpdate fires and the response carries the new updatedAt
        orderRepository.flush();
        log.info("Updated order {}", id);
        return OrderResponse.from(order);
    }

    @Transactional
    public OrderResponse updateStatus(Long id, OrderStatus status) {
        var order = findOrder(id);
        order.setStatus(status);
        orderRepository.flush();
        log.info("Order {} status -> {}", id, status);
        return OrderResponse.from(order);
    }

    @Transactional
    public void delete(Long id) {
        var order = findOrder(id);
        orderRepository.delete(order);
        log.info("Deleted order {}", id);
    }

    private Order findOrder(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));
    }
}
