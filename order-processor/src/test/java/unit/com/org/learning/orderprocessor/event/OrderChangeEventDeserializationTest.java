package com.org.learning.orderprocessor.event;

import org.junit.jupiter.api.Test;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class OrderChangeEventDeserializationTest {

    private final ObjectMapper objectMapper = JsonMapper.builder().build();

    @Test
    void deserialize_createEvent() throws Exception {
        var json = """
                {
                  "before": null,
                  "after": {
                    "id": 1,
                    "customer_id": "customer-1",
                    "product": "laptop",
                    "quantity": 2,
                    "price": "1299.99",
                    "status": "NEW",
                    "created_at": "2026-07-11T10:15:30.123456Z",
                    "updated_at": "2026-07-11T10:15:30.123456Z"
                  },
                  "source": {
                    "version": "3.1.0.Final",
                    "connector": "postgresql",
                    "name": "orders-db",
                    "ts_ms": 1783937730123,
                    "db": "orders_db",
                    "schema": "public",
                    "table": "orders",
                    "lsn": "24023928"
                  },
                  "op": "c",
                  "ts_ms": 1783937730456
                }
                """;

        var event = objectMapper.readValue(json, OrderChangeEvent.class);

        assertThat(event.op()).isEqualTo(Operation.CREATE);
        assertThat(event.before()).isNull();
        assertThat(event.after().id()).isEqualTo(1L);
        assertThat(event.after().customerId()).isEqualTo("customer-1");
        assertThat(event.after().price()).isEqualByComparingTo(new BigDecimal("1299.99"));
        assertThat(event.after().status()).isEqualTo("NEW");
        assertThat(event.source().table()).isEqualTo("orders");
    }

    @Test
    void deserialize_deleteEvent_hasOnlyBeforeImage() throws Exception {
        var json = """
                {
                  "before": {
                    "id": 1,
                    "customer_id": "customer-1",
                    "product": "laptop",
                    "quantity": 2,
                    "price": "1299.99",
                    "status": "CANCELLED",
                    "created_at": "2026-07-11T10:15:30.123456Z",
                    "updated_at": "2026-07-11T11:00:00.000000Z"
                  },
                  "after": null,
                  "source": {
                    "db": "orders_db",
                    "schema": "public",
                    "table": "orders",
                    "ts_ms": 1783941600000
                  },
                  "op": "d",
                  "ts_ms": 1783941600123
                }
                """;

        var event = objectMapper.readValue(json, OrderChangeEvent.class);

        assertThat(event.op()).isEqualTo(Operation.DELETE);
        assertThat(event.after()).isNull();
        assertThat(event.before().status()).isEqualTo("CANCELLED");
    }
}
