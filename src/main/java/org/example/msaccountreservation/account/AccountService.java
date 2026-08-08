package org.example.msaccountreservation.account;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.msaccountreservation.client.Client;
import org.example.msaccountreservation.client.ClientRepository;
import org.example.msaccountreservation.clientExceptions.ClientNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Slf4j
@Data
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    private final ClientRepository clientRepository;
    private final AccountStatusRepository accountStatusRepository;
    private  final AccountMapper accountMapper;

    @Transactional
    public AccountResponseDTO createAccount(AccountRequestDTO accountRequestDTO, UUID clientId) {
        Client foundClient = clientRepository.findById(clientId).orElseThrow(() -> {
           throw new ClientNotFoundException("Клиент с таким id не найден " + clientId);
        });

        AccountStatus accountStatus = accountStatusRepository.findByName(AccountStatusEnum.CREATED)
                .orElseThrow(()-> {
                  throw  new RuntimeException("Статус CREATED не удалсь создать");
                });

        Account account = accountMapper.accountToAccount(accountRequestDTO);
        account.setClient(foundClient);
        account.setStatus(accountStatus);

        Account accountSaved = accountRepository.save(account);
        AccountResponseDTO accountResponseDTO = accountMapper.accountToAccountDTO(accountSaved);
        return accountResponseDTO;
    }

    public AccountResponseDTO getAccount(UUID id) {
        Account account = accountRepository.findById(id).orElseThrow(() -> {
            throw new RuntimeException("аккаунт с id" + id + "не найдент");
        });

        return accountMapper.accountToAccountDTO(account);
    }
}
