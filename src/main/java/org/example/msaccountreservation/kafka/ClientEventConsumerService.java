package org.example.msaccountreservation.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.msaccountreservation.events.ClientChangedEvent;
import org.example.msaccountreservation.events.EventMapper;
import org.example.msaccountreservation.events.ProcessedEvent;
import org.example.msaccountreservation.events.ProcessedEventRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ClientEventConsumerService {
    private final EventMapper eventMapper;
    private final ProcessedEventRepository processedEventRepository;

    @Transactional
    public void processEvent(ClientChangedEvent clientChangedEvent) {
            ProcessedEvent processedEvent = eventMapper.toProcessedEvent(clientChangedEvent);
            processedEventRepository.save(processedEvent);
            log.info("Событие клиента {} сохранено в БД", processedEvent.getEventId());;
        }
}
