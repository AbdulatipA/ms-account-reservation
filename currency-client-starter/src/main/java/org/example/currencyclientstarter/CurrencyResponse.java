package org.example.currencyclientstarter;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Map;


@Data
public class CurrencyResponse {
    private boolean success;
    private String base;

    private Map<String, BigDecimal> rates;
}
