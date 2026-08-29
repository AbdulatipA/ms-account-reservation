package org.example.msaccountreservation.outbox;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.example.msaccountreservation.events.ClientChangedEvent;
import org.example.msaccountreservation.kafka.ClientEventProducer;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class OutboxProcessor {
    private final OutboxEventRepository outboxEventRepository;
    private final ClientEventProducer clientEventProducer;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Scheduled(fixedRate = 5000)
    @SchedulerLock(name = "outboxProcessorLock", lockAtMostFor = "4s", lockAtLeastFor = "2s")
    public void processOutboxEvens() {
        List<OutboxEvent> events = outboxEventRepository.findByProcessedFalse();

        for (OutboxEvent event: events) {

            try {
                ClientChangedEvent clientChangedEvent = objectMapper.readValue(
                        event.getPayload(),
                        ClientChangedEvent.class);

                clientEventProducer.sendEvent(clientChangedEvent);
                event.setProcessed(true);
                outboxEventRepository.save(event);
            } catch (Exception e) {
                log.error("Ошибка при обработки outbox события {}" , event.getId(), e);
            }

        }
    }
}
