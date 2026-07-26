import com.example.currencyclientstarter.CurrencyFeignClient;
import com.example.currencyclientstarter.CurrencyService;
import com.example.currencyclientstarter.ExchangeRateResponse;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Map;


@ExtendWith(MockitoExtension.class)
 class CurrencyServiceTest {

 @Mock
 private CurrencyFeignClient currencyClient;

 @Mock
 private MeterRegistry meterRegistry;

 private CurrencyService currencyService;

 @Mock
 private Counter counter;

//
 @BeforeEach
 void setUp() {
  currencyService = new CurrencyService("my-api-key", currencyClient, meterRegistry);
 }

 @Test
 public void shouldReturnOneWhenCurrenciesAreEqual() {
  BigDecimal rate = currencyService.getExchangeRate("USD", "USD");
  Assertions.assertEquals(BigDecimal.ONE, rate);
 }

 @Test
 public void shouldReturnCorrectExchangeRate() {
  ExchangeRateResponse expectedResponse = new ExchangeRateResponse();

  expectedResponse.setConversionRates(Map.of("EUR", new BigDecimal("0.92")));


  Mockito.when(meterRegistry.counter(Mockito.anyString())).thenReturn(counter);
  Mockito.when(currencyClient.getActualRates(Mockito.anyString(), Mockito.eq("USD")))
          .thenReturn(expectedResponse);

  BigDecimal rate = currencyService.getExchangeRate("USD", "EUR");

  Assertions.assertEquals(new BigDecimal("0.92"), rate);
 }
}
