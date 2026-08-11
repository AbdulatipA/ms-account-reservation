package org.example.msaccountreservation.account;

import jakarta.persistence.*;
import lombok.*;
import org.example.msaccountreservation.client.Client;

import java.time.Instant;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "account")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_id", nullable = false)
    private AccountStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @Column(name = "account_type", nullable = false, length = 50)
    private String accountType;

    @Column(name = "currency_code", nullable = false, length = 30)
    private String currencyCode;

    @Column(name = "create_at", nullable = false, updatable = false)
    private Instant createAt;

    @Column(name = "update_at", nullable = false)
    private Instant updateAt;

    @PrePersist
    protected void onCreate() {
        Instant now = Instant.now();
        createAt = now;
        updateAt = now;
    }

    @PreUpdate
    protected void onUpdate() {
        updateAt = Instant.now();
    }
}