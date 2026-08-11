package org.example.msaccountreservation.account;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;
import org.example.msaccountreservation.client.Client;

import java.time.Instant;
import java.util.UUID;

@JsonPropertyOrder({
        "id",
        "status",
        "client",
        "accountType",
        "currencyCode",
        "createAt",
        "updateAt"
})
@Data
public class AccountResponseDTO {
    private UUID id;
    private AccountStatus status;
    private Client client;
    private String accountType;
    private String currencyCode;
    private Instant createAt;
    private Instant updateAt;
}
