package com.example.currencyclientstarter;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@FeignClient(name = "currency-client-feign", url = "${app.currency-client.base-url}")
public interface CurrencyFeignClient {

    @GetMapping("/v6/{apikey}/latest/{sourceCurrency}")
    ExchangeRateResponse getActualRates(
        @PathVariable("apikey") String apikey,
        @PathVariable("sourceCurrency") String sourceCurrency
        );
}
