package org.example.msaccountreservation;

import com.example.currencyclientstarter.CurrencyService;
import org.example.msaccountreservation.client.Client;
import org.example.msaccountreservation.client.ClientRateDTO;
import org.example.msaccountreservation.client.ClientReportService;
import org.example.msaccountreservation.client.ClientRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.mockito.Mockito.times;


@ExtendWith(MockitoExtension.class)
public class ClientReportServiceTest {
    @Mock
    private ClientRepository clientRepository;

    @Mock
    private CurrencyService currencyService;

    private final Executor executor = Executors.newFixedThreadPool(3);
    private ClientReportService clientReportService;

    @BeforeEach
    public void setUp() {
        clientReportService = new ClientReportService(clientRepository, executor, currencyService);
    }

    //проверка бизнес логики
    @Test
    void shouldSuccessfullyMergeResults_WhenAllServicesRespond() {
        UUID clientId = UUID.randomUUID();
        Client client = new Client();
        client.setId(clientId);
        client.setFullName("Иванов Иван");

        Mockito.when(clientRepository.findById(clientId)).thenReturn(Optional.of(client));
        Mockito.when(currencyService.getExchangeRate("USD", "RUB"))
                .thenReturn(new BigDecimal("79.5407"));
        Mockito.when(currencyService.getExchangeRate("EUR", "RUB"))
                .thenReturn(new BigDecimal("91.4579"));

        ClientRateDTO clientRateDTO = clientReportService.getReport(clientId);


        Assertions.assertNotNull(clientRateDTO);
        Assertions.assertEquals(client.getId(), clientRateDTO.getClientID());
        Assertions.assertEquals("Иванов Иван", clientRateDTO.getClientName());

        BigDecimal usd = new BigDecimal("79.5407");
        BigDecimal eur = new BigDecimal("91.4579");
        Assertions.assertEquals(usd, clientRateDTO.getRates().get("USD/RUB"));
        Assertions.assertEquals(eur, clientRateDTO.getRates().get("EUR/RUB"));

        Mockito.verify(clientRepository, times(1)).findById(clientId);
        Mockito.verify(currencyService, times(1)).getExchangeRate("USD", "RUB");
        Mockito.verify(currencyService, times(1)).getExchangeRate("EUR", "RUB");
    }


    // проверка 504 эксепшена
    @Test
    void shouldExecuteCallsInParallel() {
        UUID clientId = UUID.randomUUID();
        Client client = new Client();
        client.setId(clientId);
        client.setFullName("Иванов Иван");

        Mockito.when(clientRepository.findById(clientId)).thenReturn(Optional.of(client));

        CountDownLatch countDownLatch = new CountDownLatch(1);

        BigDecimal usd = new BigDecimal("79.5407");
        BigDecimal eur = new BigDecimal("91.4579");

        Mockito.when(currencyService.getExchangeRate("USD", "RUB")).thenAnswer(invocationOnMock -> {
            countDownLatch.await(2, TimeUnit.SECONDS);
            return usd;
        });
        Mockito.when(currencyService.getExchangeRate("EUR", "RUB")).thenAnswer(invocationOnMock -> {
            countDownLatch.countDown();
            return eur;
        });

        ClientRateDTO clientRateDTO = clientReportService.getReport(clientId);

        Assertions.assertNotNull(clientRateDTO);
        Assertions.assertEquals(usd, clientRateDTO.getRates().get("USD/RUB"));
        Assertions.assertEquals(eur, clientRateDTO.getRates().get("EUR/RUB"));
    }
}
