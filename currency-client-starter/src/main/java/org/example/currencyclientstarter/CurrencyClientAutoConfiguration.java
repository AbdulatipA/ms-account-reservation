package org.example.currencyclientstarter;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
public class CurrencyClientAutoConfiguration {

    @Bean
    public CurrencyService currencyService() {
        return new CurrencyService();
    }
}
