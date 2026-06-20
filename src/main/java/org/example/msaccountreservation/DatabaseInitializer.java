package org.example.msaccountreservation;

import org.example.msaccountreservation.repository.AccountRepository;
import org.example.msaccountreservation.repository.AccountStatusRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class DatabaseInitializer implements CommandLineRunner {
    private final AccountStatusRepository accountStatusRepository;

    public DatabaseInitializer(AccountStatusRepository accountStatusRepository) {
        this.accountStatusRepository = accountStatusRepository;
    }

    @Override
    public void run(String... args){
        Map<StatusName, String> requiredStatuses = Map.of(
                StatusName.NEW, "Счёт создан в БД",
                StatusName.IN_CREATION, "Запрос на создание счета был отправлен в смежную систему",
                StatusName.CREATED, "Счёт создан в смежной системе",
                StatusName.CANCELLED, "Счёт аннулирован",
                StatusName.CLOSED, "Счет закрыт"
        );

        requiredStatuses.forEach((statusName, description) -> {
            AccountStatus status = accountStatusRepository.findByName(statusName).orElse(new AccountStatus());
            status.setName(statusName);
            status.setDescription(description);

            accountStatusRepository.save(status);
        });
    }

}
