package org.example.msaccountreservation.client;

import com.example.model.*;
import lombok.RequiredArgsConstructor;
import org.example.msaccountreservation.account.*;
import org.example.msaccountreservation.clientExceptions.ClientAlreadyExistsException;
import org.example.msaccountreservation.clientExceptions.ClientInvalidDataException;
import org.example.msaccountreservation.clientExceptions.ClientNotFoundException;

import org.example.msaccountreservation.events.*;
import org.example.msaccountreservation.kafka.ClientEventProducer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;
    private final AccountRepository accountRepository;
    private final ClientMapper clientMapper;
    private final AccountMapper accountMapper;

    private final ClientEventProducer clientEventProducer;


    @Transactional
    public ClientResponse create(ClientCreateRequest clientCreateRequest) {
        if (clientCreateRequest.getDocumentNumber() != null &&
                clientCreateRequest.getDocumentNumber().length() < 4) {
            throw new ClientInvalidDataException("Номер паспорта должен состоять не менее чем из 4 чисел");
        }

        if (clientCreateRequest.getDocumentSeries() != null &&
                clientCreateRequest.getDocumentSeries().length() < 6) {
            throw new ClientInvalidDataException("Номер паспорта должен состоять не менее чем из 6 чисел");
        }

        if (clientRepository.existsByMdmCode(clientCreateRequest.getMdmCode())) {
            throw new ClientAlreadyExistsException("Клиент с таким mdmCode уже существует");
        }

        if (clientRepository.existsByDocumentNumberAndDocumentSeries(
                clientCreateRequest.getDocumentNumber(), clientCreateRequest.getDocumentSeries()
        )) {
            throw new ClientAlreadyExistsException("Клиент с таким номером или серией паспорта уже существует");
        }


        Client client =  clientMapper.toClient(clientCreateRequest);

        Client saveClient = clientRepository.save(client);
        clientEventProducer.sendEvent(new ClientChangedEvent(
                saveClient.getId(),
                ClientTypeEvent.CREATED
        ));
        return responseClient(saveClient);
    }


    public ClientResponse responseClient(Client client) {
        ClientResponse clientResponse = clientMapper.toClientResponse(client);
        clientResponse.setStatus(ClientStatus.ACTIVE);
        clientResponse.setCreatedAt(OffsetDateTime.now());
        clientResponse.setUpdatedAt(OffsetDateTime.now());

        return clientResponse;
    }


    public ClientResponse getClientById(UUID id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ClientNotFoundException("Клиент с таким id не найден"));

        List<Account> accounts = accountRepository.findByClient(client);
        ClientResponse clientResponse = responseClient(client);
        List<AccountResponseForClient> accountResponse = accounts.stream()
                .map(accountMapper::toAccountResponseForClient)
                .toList();

        clientResponse.setHasAccounts(!accountResponse.isEmpty());
        clientResponse.setAccounts(accountResponse);
        return clientResponse;
    }


    public ClientResponse updateClientById(UUID id, PutClientById putClientById) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ClientNotFoundException("Клиент с таким id не найден"));

        client.setFullName(putClientById.getFullName());
        Client saveClient = clientRepository.save(client);

                clientEventProducer.sendEvent(new ClientChangedEvent(
                saveClient.getId(),
                ClientTypeEvent.UPDATED
        ));

        return responseClient(saveClient);



    }


    public ExistsClientResponse existsClient(Long mdmCode) {

        ExistsClientResponse existsClientResponse = new ExistsClientResponse();
        clientRepository.findByMdmCode(mdmCode)

                .ifPresentOrElse(client -> {
                    existsClientResponse.setExists(true);
                    existsClientResponse.setClientId(client.getId());
                    existsClientResponse.setStatus(ClientStatus.BLOCKED);
                }, () -> {
                    existsClientResponse.setExists(false);
                    existsClientResponse.setStatus(ClientStatus.DELETED);
                });

        return existsClientResponse;
    }


    public void deleteClient(UUID clientId) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new ClientNotFoundException("Клиент с таким id не найден"));

        clientRepository.delete(client);

        clientEventProducer.sendEvent(new ClientChangedEvent(
                client.getId(),
                ClientTypeEvent.DELETE
        ));
    }


    public GetClients getClients(Integer page, Integer size, String fullName, Long mdmCode) {
        Integer defaultPage = 0;
        Integer defaultSize = 20;

        if (page == null || page < 0) {
            page = defaultPage;
        }

        if (size == null || size < 0) {
            size = defaultSize;
        }

        Pageable pageable = PageRequest.of(page, size);

        Specification<Client> clientSpecification = Specification.where(
                ClientSpecifications.hasFullName(fullName).and(ClientSpecifications.hasMdmId(mdmCode))
        );

        Page<Client> clientPage = clientRepository.findAll(clientSpecification, pageable);

        GetClients getClients = new GetClients();


        List<GetClientsContentInner> getClientsPageables = clientPage.getContent().stream()
                .map(client -> {
                    GetClientsContentInner inner = new GetClientsContentInner();
                    inner.setId(client.getId());
                    inner.setFullName(client.getFullName());
                    inner.setStatus(ClientStatus.ACTIVE);

                    long activeAccount = accountRepository.countByClientAndStatusName(client, AccountStatusEnum.CREATED);
                    inner.setNumberOfActiveAccounts(activeAccount);

                    return inner;
                }).collect(Collectors.toList());

        getClients.setContent(getClientsPageables);

        return getClients;
    };
}