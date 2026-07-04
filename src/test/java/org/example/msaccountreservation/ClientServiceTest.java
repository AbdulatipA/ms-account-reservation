package org.example.msaccountreservation;

import com.example.model.*;
import org.example.msaccountreservation.client.Client;
import org.example.msaccountreservation.client.ClientService;
import org.example.msaccountreservation.client.ClientRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class ClientServiceTest {

    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private ClientService clientService;


    @Test
    public void create() {
        Mockito.when(clientRepository.existsByMdmCode(any())).thenReturn(false);
        Mockito.when(clientRepository.existsByDocumentNumberAndDocumentSeries(any(), any())).thenReturn(false);
        Mockito.when(clientRepository.save(any(Client.class))).thenAnswer(AdditionalAnswers.returnsFirstArg());

        ClientResponse actualRequest = clientService.create(References.requestJson());
        ClientResponse expectedResponse = References.responseJson();

        Assertions.assertEquals(expectedResponse.getFullName(), actualRequest.getFullName());
        Assertions.assertEquals(expectedResponse.getDocumentNumber(), actualRequest.getDocumentNumber());
        Assertions.assertEquals(expectedResponse.getDocumentSeries(), actualRequest.getDocumentSeries());
        Assertions.assertEquals(expectedResponse.getMdmCode(), actualRequest.getMdmCode());
    }


    @Test
    public void getClientById() {

        UUID id = UUID.randomUUID();
        Client client = new Client();
        client.setId(id);
        client.setFullName(References.requestJson().getFullName());
        client.setMdmCode(References.requestJson().getMdmCode());

        Mockito.when(clientRepository.findById(any())).thenReturn(Optional.of(client));

        ClientResponse actualResponse = clientService.getClientById(id);

        Assertions.assertEquals(client.getFullName(), actualResponse.getFullName());
        Assertions.assertEquals(client.getId(), actualResponse.getId());
        Assertions.assertEquals(client.getMdmCode(), actualResponse.getMdmCode());
    }


    @Test
    public void putClientById() {

        UUID id = UUID.randomUUID();

        Client client = new Client();
        client.setId(id);
        client.setFullName(References.requestJson().getFullName());

        PutClientById putClient = new PutClientById();
        putClient.setFullName("Иванов Иван Иваныч");

        Mockito.when(clientRepository.findById(any())).thenReturn(Optional.of(client));

        clientService.putClientById(id, putClient);

        Assertions.assertEquals(client.getFullName(), putClient.getFullName());
    }


    @Test
    public void existsClient() {
        UUID id = UUID.randomUUID();

        ExistsClientResponse existsClient = new ExistsClientResponse();
        existsClient.clientId(id);
        existsClient.setStatus(ClientStatus.BLOCKED);

        Client client = new Client();
        client.setId(id);

        Mockito.when(clientRepository.findByMdmCode(any())).thenReturn(Optional.of(client));

        ExistsClientResponse actualResponse = clientService.existsClient(References.requestJson().getMdmCode());

        Assertions.assertEquals(existsClient.getClientId(), actualResponse.getClientId());
        Assertions.assertEquals(existsClient.getStatus(), actualResponse.getStatus());
    }


    @Test
    public void deleteClient() {

        UUID id = UUID.randomUUID();
        Client client = new Client();
        client.setId(id);

        Mockito.when(clientRepository.findById(any())).thenReturn(Optional.of(client));

        clientService.deleteClient(id);

        Mockito.verify(clientRepository, Mockito.times(1)).delete(client);
    }


    @Test
    public void getClients() {
        // 1. ГОТОВИМ ДАННЫЕ
        // Создаем список клиентов, который «как будто вернула БД»
        Client client1 = new Client();
        client1.setId(UUID.randomUUID());
        client1.setFullName("Иванов Иван Иванович");

        Client client2 = new Client();
        client2.setId(UUID.randomUUID());
        client2.setFullName("Петров Петр Петрович");

        List<Client> clientList = List.of(client1, client2);

        // Собираем виртуальную страницу (список клиентов, настройки пагинации, общее количество)
        Pageable pageable = PageRequest.of(0, 20);
        Page<Client> mockPage = new PageImpl<>(clientList, pageable, clientList.size());

        // 2. ОБУЧАЕМ MOCKITO
        // Говорим репозиторию: при любом вызове findAll со спецификацией возвращать нашу mockPage
        Mockito.when(clientRepository.findAll(any(Specification.class), any(Pageable.class)))
                .thenReturn(mockPage);

        // 3. ВЫЗЫВАЕМ РЕАЛЬНЫЙ СЕРВИС
        GetClients actualResponse = clientService.getClients(0, 20, "Иван", 11233L);

        // 4. ПРОВЕРЯЕМ РЕЗУЛЬТАТ (Сравниваем то, что вышло из маппера, с нашими ожиданиями)
        Assertions.assertNotNull(actualResponse);
        Assertions.assertEquals(2, actualResponse.getContent().size()); // Должно быть 2 клиента в списке
        Assertions.assertEquals("Иванов Иван Иванович", actualResponse.getContent().get(0).getFullName());

        // Проверяем метаданные пагинации в блоке pageable
        Assertions.assertEquals(0, actualResponse.getPageable().getPageNumber());
        Assertions.assertEquals(2, actualResponse.getPageable().getTotalElements());
    }
}
