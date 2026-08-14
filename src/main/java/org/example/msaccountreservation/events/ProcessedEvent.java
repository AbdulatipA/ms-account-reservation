package org.example.msaccountreservation.events;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@Table(name = "processed_events")
public class ProcessedEvent {

    @Id
    @Column(name = "event_id", nullable = false)
    private String eventId;

    @Column(name = "client_id", nullable = false)
    private UUID clientId;

    @Column(name = "timestamp", nullable = false)
    private Instant timestamp;
}
