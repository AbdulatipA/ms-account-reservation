package com.example.currencyclientstarter;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.health.contributor.Health;
import org.springframework.boot.health.contributor.HealthIndicator;

@Data
@RequiredArgsConstructor
public class CurrencyServiceHealthIndicator implements HealthIndicator {
    private final CurrencyFeignClient feignClient;
    private final CurrencyProperties properties;


    @Override
    public Health health() {
        try {
            // Делаем легкий запрос к вашему Feign-клиенту для проверки связи
            feignClient.getActualRates(properties.getApiKey(), "USD");

            return Health.up()
                    .withDetail("provider", "Currency API")
                    .build();
        } catch (Exception e) {
            // Если API лежит или ключ невалидный — сервис DOWN
            return Health.down()
                    .withDetail("provider", "Currency API")
                    .withDetail("error", e.getMessage())
                    .build();
        }
    }
}
