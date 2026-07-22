package com.example.currencyclientstarter;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.health.contributor.HealthIndicator;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
@EnableConfigurationProperties(CurrencyProperties.class)
@ConditionalOnProperty(prefix = "app.currency-client", name = "enabled")
@EnableFeignClients(basePackages = "com.example.currencyclientstarter")
public class CurrencyAutoConfiguration {

    @Bean
    public CurrencyService currencyService(CurrencyProperties currencyProperties, CurrencyFeignClient feignClient) {
        return new CurrencyService(currencyProperties.getApiKey(), feignClient);
    }


    @Bean
    @ConditionalOnClass(HealthIndicator.class)
    public CurrencyServiceHealthIndicator currencyServiceHealthIndicator(
            CurrencyFeignClient feignClient,
            CurrencyProperties currencyProperties) {
        return new CurrencyServiceHealthIndicator(feignClient, currencyProperties);
    }
}
