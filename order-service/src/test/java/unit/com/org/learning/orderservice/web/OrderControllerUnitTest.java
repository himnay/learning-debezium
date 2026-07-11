package com.org.learning.orderservice.web;

import com.org.learning.orderservice.domain.OrderStatus;
import com.org.learning.orderservice.service.OrderService;
import com.org.learning.orderservice.web.dto.OrderRequest;
import com.org.learning.orderservice.web.dto.OrderResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.time.Instant;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)
class OrderControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private OrderService orderService;

    @Test
    void createOrder_validRequest_returns201() throws Exception {
        var request = new OrderRequest("customer-1", "laptop", 1, new BigDecimal("999.99"));
        var response = new OrderResponse(1L, "customer-1", "laptop", 1,
                new BigDecimal("999.99"), OrderStatus.NEW, Instant.now(), Instant.now());
        when(orderService.create(any(OrderRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.status").value("NEW"));
    }

    @Test
    void createOrder_invalidRequest_returns400() throws Exception {
        var invalid = new OrderRequest(null, "", 0, new BigDecimal("-1"));

        mockMvc.perform(post("/api/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getOrder_unknownId_returns404() throws Exception {
        when(orderService.get(anyLong()))
                .thenThrow(new com.org.learning.orderservice.exception.OrderNotFoundException(99L));

        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                        .get("/api/v1/orders/99"))
                .andExpect(status().isNotFound());
    }
}
