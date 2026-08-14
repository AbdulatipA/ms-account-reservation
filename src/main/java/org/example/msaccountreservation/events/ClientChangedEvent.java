package org.example.msaccountreservation.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientChangedEvent {
    private String eventId;
    private UUID clientId;
    private ClientTypeEvent clientType;
    private Instant timestamp;

    public ClientChangedEvent(UUID clientId, ClientTypeEvent clientType) {
        this.clientId = clientId;
        this.clientType = clientType;
    }
}
