import com.example.currencyclientstarter.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.util.Map;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = CurrencyClientStarterApplication.class,
        properties = {
        "app.currency-client.enabled=true",
        "app.currency-client.base-url=localhost:8080",
        "app.currency-client.api-key=test-key",
        "spring.cache.type=redis"
})
@Testcontainers
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@EnableCaching
public class CurrencyServiceIntegrationTest {

    @Autowired
    private CurrencyService currencyService;

    @Autowired(required = false)
    private CacheManager cacheManager;

    @MockitoBean
    private CurrencyFeignClient currencyFeignClient;

    @Container
    static final GenericContainer<?> redis = new GenericContainer<>("redis:7-alpine").withExposedPorts(6379);


    @DynamicPropertySource
    static void redisProperty(DynamicPropertyRegistry registry) {
        registry.add("spring.data.redis.host", redis::getHost); // Было spring.redis.host
        registry.add("spring.data.redis.port", redis::getFirstMappedPort); // Было spring.redis.port
    }

    @BeforeEach
    void cleanCache() {
        Cache cache = cacheManager.getCache("CURRENCY_CACHE");
        if (cache != null) {
            cache.clear();
        }
    }


    @Test
    public void getExchangeRate() {
        ExchangeRateResponse exchangeRateResponse = new ExchangeRateResponse();
        exchangeRateResponse.setConversionRates(Map.of("EUR", new BigDecimal("0.92")));

        when(currencyFeignClient.getActualRates(anyString(), Mockito.eq("USD")))
                .thenReturn(exchangeRateResponse);

        currencyService.getExchangeRate("USD", "EUR");
        currencyService.getExchangeRate("USD", "EUR");

        Mockito.verify(currencyFeignClient, Mockito.times(1))
                .getActualRates(anyString(), Mockito.eq("USD"));
    }

}
