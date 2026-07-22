package com.example.currencyclientstarter;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "app.currency-client")
public class CurrencyProperties {
    private String baseUrl;
    private String apiKey;
    private boolean enabled = true;
}
