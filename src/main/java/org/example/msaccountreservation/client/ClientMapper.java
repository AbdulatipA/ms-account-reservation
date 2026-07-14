package org.example.msaccountreservation.client;

import com.example.model.ClientCreateRequest;
import com.example.model.ClientResponse;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface  ClientMapper {
    Client toClient(ClientCreateRequest clientCreateRequest);
    ClientResponse toClientResponse(Client client);
}
