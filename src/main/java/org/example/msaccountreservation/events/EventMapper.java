package org.example.msaccountreservation.events;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EventMapper {

    @Mapping(target = "eventId", source = "eventId")
    @Mapping(target = "clientId", source = "clientId")
    @Mapping(target = "timestamp", source = "timestamp")
    ProcessedEvent toProcessedEvent(ClientChangedEvent clientChangedEvent);
}
