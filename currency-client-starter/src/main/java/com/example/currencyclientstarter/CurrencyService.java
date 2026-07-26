package com.example.currencyclientstarter;

import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;

import java.math.BigDecimal;


@RequiredArgsConstructor
@Slf4j
public class CurrencyService {
    private final String apiKey;
    private final CurrencyFeignClient currencyClient;
    private final MeterRegistry meterRegistry;


    @Cacheable(value = "CURRENCY_CACHE", key = "#root.args")
    public BigDecimal getExchangeRate(String fromCurrency, String toCurrency) {
        log.info("getExchangeRate: from {} to {}", fromCurrency, toCurrency);

        if (fromCurrency.equalsIgnoreCase(toCurrency)) {
            return BigDecimal.ONE;
        }

        meterRegistry.counter("currency.exchange.rate.requests").increment();

        ExchangeRateResponse response = currencyClient.getActualRates(apiKey, fromCurrency);

        if (response == null || response.getConversionRates() == null) {
            throw new IllegalStateException("API валют вернуло пустой ответ");
        }

        BigDecimal rate = response.getConversionRates().get(toCurrency);
        if (rate == null) {
            throw new IllegalArgumentException("Валюта " + toCurrency + " не найдена в ответе API");
        }

        return rate;
    }
}
