package org.example.msaccountreservation.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.msaccountreservation.events.ClientChangedEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ClientEventConsumer {
    private final ClientEventConsumerService clientEventConsumerService;

    @KafkaListener(
            topics = "${app.kafka.client-topic-name}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "exactlyOnceKafkaListenerContainerFactory"
    )
    public void listen(ClientChangedEvent clientChangedEvent,
                       @Header(KafkaHeaders.RECEIVED_PARTITION) int partition) {

        log.info("Номер партиции: {}, id клиента : {}, clientType: {}, instant: {}",
                partition,
                clientChangedEvent.getClientId(),
                clientChangedEvent.getClientType(),
                clientChangedEvent.getTimestamp());

        try {
            clientEventConsumerService.processEvent(clientChangedEvent);
            log.info("Сообщение обработано, оффсет закомичен");
        } catch (Exception e) {
            log.error("Ошибка при обработки сообщения {}: {}", clientChangedEvent, e.getMessage());
            throw e;
        }
    }
}
