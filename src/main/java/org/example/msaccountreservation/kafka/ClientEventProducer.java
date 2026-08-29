package org.example.msaccountreservation.kafka;

import lombok.extern.slf4j.Slf4j;
import org.example.msaccountreservation.events.ClientChangedEvent;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class ClientEventProducer {
    private final KafkaTemplate<String, ClientChangedEvent> kafkaTemplate;

    private String topicName;

    public ClientChangedEvent sendEvent(ClientChangedEvent clientChangedEvent){
        String partitionKey = clientChangedEvent.getClientId().toString();

        kafkaTemplate.send(topicName, partitionKey, clientChangedEvent)
                        .whenComplete((result, ex) -> {
                            if (ex == null) {
                                log.info("Сообщение отправленно в топик={}, partition={}, offset={}",
                                        topicName,
                                        result.getRecordMetadata().partition(),
                                        result.getRecordMetadata().offset());
                            } else {
                                log.error("Ошибка отправки события в Kafka: ", ex);
                            }
                        });

        return clientChangedEvent;
    }

    public ClientEventProducer(
            @Qualifier("exactlyOnceKafkaTemplate") KafkaTemplate<String, ClientChangedEvent> kafkaTemplate,
            @Value("${spring.kafka.client-topic-name}") String topicName) {
        this.kafkaTemplate = kafkaTemplate;
        this.topicName = topicName;
    }
}
