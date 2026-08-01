package org.example.msaccountreservation.client;

import com.example.currencyclientstarter.CurrencyService;
import org.example.msaccountreservation.clientExceptions.ClientGatewayTimeout;
import org.example.msaccountreservation.clientExceptions.ClientNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.*;

@Service
public class ClientReportService {
    private static final Logger log = LoggerFactory.getLogger(ClientReportService.class);
    private final ClientRepository clientRepository;
    private final Executor executor;
    private final CurrencyService currencyService;

    public ClientReportService(ClientRepository clientRepository,
                               @Qualifier("currencyExecutor") Executor executor, CurrencyService currencyService) {
        this.clientRepository = clientRepository;
        this.executor = executor;
        this.currencyService = currencyService;
    }

    public ClientRateDTO getReport(UUID clientId) {
        //Запрос в бд для получения клиента
        CompletableFuture<Client> clientFuture = CompletableFuture.supplyAsync(() -> {
           return clientRepository.findById(clientId).orElseThrow(() ->
                    new ClientNotFoundException("Клиент с таким id не найден"));
        }, executor).orTimeout(30, TimeUnit.SECONDS);

        //мапа, чтобы складывать туда курсы валют
        Map<String, BigDecimal> rateMap = new ConcurrentHashMap<>();

        CompletableFuture<Void> rateUSDFuture = CompletableFuture.runAsync(() -> {
            BigDecimal exchangeRate = currencyService.getExchangeRate("USD", "RUB");
            rateMap.put("USD/RUB", exchangeRate);
        }, executor).orTimeout(5, TimeUnit.SECONDS);




        CompletableFuture<Void> rateEURFuture = CompletableFuture.runAsync(() -> {
            // с имитировал задержку
//            try {
//                Thread.sleep(6000);
//            } catch (InterruptedException e) {
//                Thread.currentThread().interrupt();
//            }

            BigDecimal exchangeRate = currencyService.getExchangeRate("EUR", "RUB");
            rateMap.put("EUR/RUB", exchangeRate);
        }, executor).orTimeout(5, TimeUnit.SECONDS);


        CompletableFuture<Void> allTasksFuture = CompletableFuture.allOf(
                clientFuture,
                rateUSDFuture,
                rateEURFuture
        );

        return allTasksFuture.handle((v, e) -> {

            if (e != null) {
                if (clientFuture.isCompletedExceptionally()) {
                    clientFuture.join();
                }
                throw new ClientGatewayTimeout("Сервис неотвечает, попробуйте позже");
            }

            Client client = clientFuture.join();
            ClientRateDTO clientRateDTO = new ClientRateDTO();

            clientRateDTO.setClientName(client.getFullName());
            clientRateDTO.setClientID(client.getId());
            clientRateDTO.setRates(rateMap);

            return clientRateDTO;
        }).join();
    }
}
