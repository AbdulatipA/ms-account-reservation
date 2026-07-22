package com.example.currencyclientstarter;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.cache.autoconfigure.RedisCacheManagerBuilderCustomizer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.health.contributor.HealthIndicator;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import java.math.BigDecimal;
import java.time.Duration;

@AutoConfiguration
@EnableConfigurationProperties(CurrencyProperties.class)
@ConditionalOnProperty(prefix = "app.currency-client", name = "enabled")
@EnableFeignClients(basePackages = "com.example.currencyclientstarter")
@EnableCaching
public class CurrencyAutoConfiguration {

    @Bean
    public CurrencyService currencyService(CurrencyProperties currencyProperties,
                                           CurrencyFeignClient feignClient,
                                           MeterRegistry meterRegistry) {

        return new CurrencyService(currencyProperties.getApiKey(), feignClient, meterRegistry);
    }


    @Bean
    @ConditionalOnClass(HealthIndicator.class)
    public CurrencyServiceHealthIndicator currencyServiceHealthIndicator(
            CurrencyFeignClient feignClient,
            CurrencyProperties currencyProperties) {
        return new CurrencyServiceHealthIndicator(feignClient, currencyProperties);
    }


    @Bean
    public RedisCacheManagerBuilderCustomizer redisCacheManagerBuilderCustomizer() {
        return (builder) ->
                builder.withCacheConfiguration("half_hour_cache",
                        RedisCacheConfiguration.defaultCacheConfig()
                                .entryTtl(Duration.ofMinutes(30))
                                .disableCachingNullValues()
                                .serializeValuesWith(RedisSerializationContext.SerializationPair
                                        .fromSerializer(new GenericToStringSerializer<>(BigDecimal.class)))
                );
    }
}
