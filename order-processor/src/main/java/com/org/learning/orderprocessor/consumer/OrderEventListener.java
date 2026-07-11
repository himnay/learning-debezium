package com.org.learning.orderprocessor.consumer;

import com.org.learning.orderprocessor.event.OrderChangeEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderEventListener {

    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "${app.kafka.topics.orders}")
    public void onOrderChange(ConsumerRecord<String, String> consumerRecord) {
        // Debezium emits a tombstone (null value) after each delete for log compaction
        if (consumerRecord.value() == null) {
            log.info("Tombstone received, key={}", consumerRecord.key());
            return;
        }

        var event = objectMapper.readValue(consumerRecord.value(), OrderChangeEvent.class);
        switch (event.op()) {
            case CREATE -> log.info("Order created: {}", event.after());
            case UPDATE -> log.info("Order updated: before={}, after={}", event.before(), event.after());
            case DELETE -> log.info("Order deleted: {}", event.before());
            case READ -> log.info("Order snapshot: {}", event.after());
        }
    }
}
