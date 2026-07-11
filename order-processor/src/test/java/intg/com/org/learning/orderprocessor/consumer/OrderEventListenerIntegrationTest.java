package com.org.learning.orderprocessor.consumer;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

@SpringBootTest
@ActiveProfiles("local")
@EmbeddedKafka(topics = "orders-db.public.orders", partitions = 1)
@TestPropertySource(properties = {
        "spring.kafka.consumer.bootstrap-servers=${spring.embedded.kafka.brokers}",
        "spring.kafka.consumer.auto-offset-reset=earliest"
})
class OrderEventListenerIntegrationTest {

    @Autowired
    private EmbeddedKafkaBroker embeddedKafkaBroker;

    @MockitoSpyBean
    private OrderEventListener orderEventListener;

    @Test
    void consumesDebeziumChangeEvent() throws Exception {
        var payload = """
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
                  "source": {"db": "orders_db", "schema": "public", "table": "orders", "ts_ms": 1783937730123},
                  "op": "c",
                  "ts_ms": 1783937730456
                }
                """;

        var producerProps = KafkaTestUtils.producerProps(embeddedKafkaBroker);
        var producerFactory = new DefaultKafkaProducerFactory<>(
                producerProps, new StringSerializer(), new StringSerializer());
        var kafkaTemplate = new KafkaTemplate<>(producerFactory);
        kafkaTemplate.send(new ProducerRecord<>("orders-db.public.orders", "{\"id\":1}", payload)).get();

        verify(orderEventListener, timeout(10_000)).onOrderChange(any());
    }
}
