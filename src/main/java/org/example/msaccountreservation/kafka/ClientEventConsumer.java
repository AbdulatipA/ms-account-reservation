package org.example.msaccountreservation.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.msaccountreservation.events.ClientChangedEvent;
import org.example.msaccountreservation.events.EventMapper;
import org.example.msaccountreservation.events.ProcessedEvent;
import org.example.msaccountreservation.events.ProcessedEventRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClientEventConsumer {
    private final EventMapper eventMapper;
    private final ProcessedEventRepository processedEventRepository;

    @KafkaListener(
            topics = "${app.kafka.client-topic-name}",
            groupId = "${spring.kafka.consumer.group-id}"
    )
    public void listen(ClientChangedEvent clientChangedEvent,
                       Acknowledgment ack,
                       @Header(KafkaHeaders.RECEIVED_PARTITION) int partition) {

        try {
            ProcessedEvent processedEvent = eventMapper.toProcessedEvent(clientChangedEvent);
            processedEventRepository.save(processedEvent);

            ack.acknowledge();
            log.info("Сообщение обработано, оффсет закомичен");
            log.info("Номер партиции: {}", partition);
            log.info("id клиента : {}", clientChangedEvent.getClientId());
            log.info("clientType: {}", clientChangedEvent.getClientType());
            log.info("instant: {}", clientChangedEvent.getTimestamp());
        } catch (Exception e) {
            log.error("Ошибка при обработки сообщения {}: {}", clientChangedEvent, e.getMessage());
            throw e;
        }
    }
}
