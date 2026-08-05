package org.example.msaccountreservation.client;

import com.example.api.ClientsApi;
import com.example.currencyclientstarter.CurrencyService;
import com.example.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.UUID;


@RestController
@RequestMapping("${api.prefix}")
@RequiredArgsConstructor
public class ClientController implements ClientsApi {
    private final ClientService clientService;
    private final CurrencyService currencyService;
    private final ClientReportService clientReportService;

    @Override
    public ResponseEntity<ClientResponse> createClient(ClientCreateRequest clientCreateRequest) {
        ClientResponse response = clientService.create(clientCreateRequest);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<Void> deleteClient(UUID clientId) {
        clientService.deleteClient(clientId);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<ExistsClientResponse> existsClient(Long clientId) {
        return ResponseEntity.ok(clientService.existsClient(clientId));
    }

    @Override
    public ResponseEntity<GetClients> getAllClients(Integer page, Integer size, String fullName, Long mdmId) {
        return ResponseEntity.ok(clientService.getClients(page, size, fullName, mdmId));
    }

    @Override
    public ResponseEntity<ClientResponse> getClientById(UUID clientId) {
        return ResponseEntity.ok(clientService.getClientById(clientId));
    }

    @Override
    public ResponseEntity<ClientResponse> updateClient(UUID clientId, PutClientById putClientById) {
        return ResponseEntity.ok(clientService.updateClientById(clientId, putClientById));
    }

    @GetMapping("/{from}/{to}/rate")
    public ResponseEntity<BigDecimal> getRate(@PathVariable("from") String fromCurrency, @PathVariable("to") String toCurrency) {
        BigDecimal rate = currencyService.getExchangeRate(fromCurrency, toCurrency);
        return ResponseEntity.ok(rate);
    }

    @GetMapping("/clients/{clientId}/summary")
    public ResponseEntity<ClientRateDTO> getClientRateSummary(@PathVariable("clientId") UUID clientId){
      return ResponseEntity.ok(clientReportService.getReport(clientId));
    };
}