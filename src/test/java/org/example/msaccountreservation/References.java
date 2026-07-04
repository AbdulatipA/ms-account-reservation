package org.example.msaccountreservation;

import com.example.model.ClientCreateRequest;
import com.example.model.ClientResponse;

public class References {

    public static final ClientCreateRequest requestJson() {
        ClientCreateRequest clientCreateRequest = new ClientCreateRequest();

        clientCreateRequest.setFullName("Иванов Иван Иванович");
        clientCreateRequest.setClientType("INDIVIDUAL");
        clientCreateRequest.setDocumentType("PASSPORT");
        clientCreateRequest.setDocumentNumber("1232");
        clientCreateRequest.setDocumentSeries("123346");
        clientCreateRequest.setMdmCode(11233L);

        return clientCreateRequest;
    }

    public static final ClientResponse responseJson() {
        ClientResponse clientResponse = new ClientResponse();

        clientResponse.setFullName("Иванов Иван Иванович");
        clientResponse.setClientType("INDIVIDUAL");
        clientResponse.setDocumentType("PASSPORT");
        clientResponse.setDocumentNumber("1232");
        clientResponse.setDocumentSeries("123346");
        clientResponse.setMdmCode(11233L);

        return clientResponse;
    }

}
