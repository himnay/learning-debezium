package com.org.learning.orderprocessor.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.FixedBackOff;

@Configuration
@Slf4j
public class KafkaConsumerConfig {

    /**
     * Retry a failed record twice with a 1s pause, then log and skip it —
     * a poison message must not block the CDC stream.
     */
    @Bean
    public DefaultErrorHandler errorHandler() {
        var errorHandler = new DefaultErrorHandler(
                (consumerRecord, exception) ->
                        log.error("Skipping record after retries, topic={}, offset={}",
                                consumerRecord.topic(), consumerRecord.offset(), exception),
                new FixedBackOff(1000L, 2));
        return errorHandler;
    }
}
