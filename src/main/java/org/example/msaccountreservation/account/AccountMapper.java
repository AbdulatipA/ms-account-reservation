package org.example.msaccountreservation.account;

import com.example.model.AccountResponseForClient;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AccountMapper {
    Account accountToAccount(AccountRequestDTO accountRequestDTO);
    AccountResponseDTO accountToAccountDTO(Account account);

    @Mapping(target = "status", source = "account.status.name")
    @Mapping(target = "accountNumber", source = "id")
    AccountResponseForClient toAccountResponseForClient(Account account);
}
