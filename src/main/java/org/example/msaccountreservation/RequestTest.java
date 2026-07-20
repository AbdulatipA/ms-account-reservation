package org.example.msaccountreservation;

import com.example.currencyclientstarter.CurrencyService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class RequestTest implements CommandLineRunner {
    private final CurrencyService currencyService;


    @Override
    public void run(String... args) throws Exception {
        System.out.println("тут начинает работу RequestTest");

        BigDecimal rate = currencyService.getExchangeRate("USD", "RUB");

        System.out.println("Результат выполнения: " + rate);
        System.out.println("=====================================");
    }
}
