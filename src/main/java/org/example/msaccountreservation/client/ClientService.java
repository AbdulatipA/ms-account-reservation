package org.example.msaccountreservation.client;

import com.example.model.*;
import lombok.RequiredArgsConstructor;
import org.example.msaccountreservation.clientExceptions.ClientAlreadyExistsException;
import org.example.msaccountreservation.clientExceptions.ClientInvalidDataException;
import org.example.msaccountreservation.clientExceptions.ClientNotFoundException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;

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
        return responseClient(saveClient);
    }


    public ClientResponse responseClient(Client client) {
        ClientResponse clientResponse = new ClientResponse();

        clientResponse = clientMapper.toClientResponse(client);

        clientResponse.setId(client.getId());
        clientResponse.setMdmCode(client.getMdmCode());
        clientResponse.setFullName(client.getFullName());
        clientResponse.citizenship(client.getCitizenship());
        clientResponse.clientType(client.getClientType());
        clientResponse.documentNumber(client.getDocumentNumber());
        clientResponse.documentSeries(client.getDocumentSeries());
        clientResponse.documentType(client.getDocumentType());
        clientResponse.mdmCode(client.getMdmCode());
        clientResponse.setStatus(ClientStatus.BLOCKED);
        clientResponse.setCreatedAt(OffsetDateTime.now());
        clientResponse.setUpdatedAt(OffsetDateTime.now());

        clientResponse.setHasAccounts(false);

        return clientResponse;
    }


    public ClientResponse getClientById(UUID id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ClientNotFoundException("Клиент с таким id не найден"));
        return responseClient(client);
    }


    public ClientResponse updateClientById(UUID id, PutClientById putClientById) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ClientNotFoundException("Клиент с таким id не найден"));

        client.setFullName(putClientById.getFullName());
        clientRepository.save(client);
        return responseClient(client);
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
    }


    public GetClients getClients(Integer page, Integer size, String fullName, Long mdmId) {

        // 1. Задаем значения по умолчанию для пагинации, если параметры не переданы
        int pageNumber = (page != null) ? page : 0;
        int pageSize = (size != null) ? size : 20;
        Pageable pageable = PageRequest.of(pageNumber, pageSize);


        // 2. Строим динамический запрос в зависимости от переданных фильтров
        Specification<Client> spec = (Specification.where(ClientSpecifications.hasFullName(fullName))
                .and(ClientSpecifications.hasMdmId(mdmId)));

        // 3. Делаем запрос в базу данных
        Page<Client> clientPage = clientRepository.findAll(spec, pageable);

        // 4. Маппим полученную страницу сущностей в объект ответа GetClients из OpenAPI
        GetClients getClientsResponse = new GetClients();

        // Сборка списка контента (внутренних элементов)
        List<GetClientsContentInner> contentList = clientPage.getContent().stream()
                .map(client -> {
                    GetClientsContentInner item = new GetClientsContentInner();
                    item.setId(client.getId());
                    item.setFullName(client.getFullName());
                    // Присваиваем статус (вручную ACTIVE или берем из client, если он там есть)
                    item.setStatus(ClientStatus.ACTIVE);
                    return item;
                })
                .collect(java.util.stream.Collectors.toList());
        getClientsResponse.setContent(contentList);

        // Сборка метаданных пагинации
        GetClientsPageable responsePageable = new GetClientsPageable();
        responsePageable.setPageNumber(clientPage.getNumber());
        responsePageable.setPageSize(clientPage.getSize());
        responsePageable.setTotalPages(clientPage.getTotalPages());
        responsePageable.setTotalElements(Math.toIntExact(clientPage.getTotalElements()));
        getClientsResponse.setPageable(responsePageable);

        return getClientsResponse;
    }
}