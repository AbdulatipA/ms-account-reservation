package org.example.msaccountreservation.events;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@NoArgsConstructor
public class ClientChangedEvent {
    private String eventId;
    private UUID clientId;
    private ClientTypeEvent clientType;
    private Instant timestamp;

    public ClientChangedEvent(String eventId, UUID clientId, ClientTypeEvent clientType, Instant timestamp) {
        this.eventId = eventId;
        this.clientId = clientId;
        this.clientType = clientType;
        this.timestamp = timestamp;
    }
}
