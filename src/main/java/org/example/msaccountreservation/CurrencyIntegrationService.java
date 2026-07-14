package org.example.msaccountreservation;

import lombok.extern.slf4j.Slf4j;
import org.example.currencyclientstarter.CurrencyService;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

import java.math.BigDecimal;

@Slf4j
@Service
public class CurrencyIntegrationService {
    private final CurrencyService currencyService;

    public CurrencyIntegrationService(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    public BigDecimal getSafeExchangeRate(String fromCurrency, String toCurrency) {
        try {
            // Вызываем метод получения курса из нашего стартера
            return currencyService.getExchangeRate(fromCurrency, toCurrency);

        } catch (RestClientException e) {
            // Сюда мы попадем, если упадет сеть, API вернет 404, 500 и т.д.
            log.info("Произошла HTTP-ошибка при запросе курса валют: " + e.getMessage());

            // Возвращаем безопасный дефолт (например, 0), чтобы приложение не падало
            return BigDecimal.ZERO;
        }
    }
}
