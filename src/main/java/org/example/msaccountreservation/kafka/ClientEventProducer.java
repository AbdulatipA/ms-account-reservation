package org.example.msaccountreservation.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.msaccountreservation.events.ClientChangedEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClientEventProducer {
    private final KafkaTemplate<String, ClientChangedEvent> kafkaTemplate;

    @Value("${app.kafka.client-topic-name}")
    private String topicName;

    public ClientChangedEvent sendEvent(ClientChangedEvent clientChangedEvent){
        String partitionKey = clientChangedEvent.getClientId().toString();

        if (clientChangedEvent.getEventId() == null) {
            clientChangedEvent.setEventId(UUID.randomUUID().toString());
        }

        clientChangedEvent.setTimestamp(Instant.now());

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
}
