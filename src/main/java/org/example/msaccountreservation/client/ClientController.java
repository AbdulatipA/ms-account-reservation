package org.example.msaccountreservation.client;

import com.example.api.ClientsApi;
import com.example.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;


@RestController
@RequestMapping("${api.prefix}")
@RequiredArgsConstructor
public class ClientController implements ClientsApi {
    private final ClientService clientService;

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
}