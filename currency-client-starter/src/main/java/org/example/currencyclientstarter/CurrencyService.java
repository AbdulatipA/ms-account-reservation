package org.example.currencyclientstarter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Map;

public class CurrencyService {
    @Value("${app.currency-client.base-url}")
    private String baseUrl;

    @Value("${app.currency-client.api-keyapp.cu}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    public BigDecimal getExchangeRate(String fromCurrency, String toCurrency) {
        // Строго по тексту формируем URL для вызова публичного API
        String url = baseUrl + "/latest?base=" + fromCurrency;

        // Запрашиваем ответ сразу в виде сырой Map (дерева объектов), не создавая никаких своих классов
        Map response = restTemplate.getForObject(url, Map.class);

        if (response != null && response.containsKey("rates")) {
            // Извлекаем внутренний объект "rates", который тоже является Map
            Map rates = (Map) response.get("rates");

            if (rates != null && rates.containsKey(toCurrency)) {
                // Достаем значение курса (например, для "RUB") и превращаем его в BigDecimal
                Object rateValue = rates.get(toCurrency);
                return new BigDecimal(rateValue.toString());
            }
        }

        return null;
    }
}
