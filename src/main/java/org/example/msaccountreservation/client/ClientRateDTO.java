package org.example.msaccountreservation.client;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

@Data
public class ClientRateDTO {
    UUID clientID;
    String clientName;
    Map<String, BigDecimal> rates;
}
