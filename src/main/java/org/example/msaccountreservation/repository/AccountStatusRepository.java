package org.example.msaccountreservation.repository;

import org.example.msaccountreservation.account.AccountStatus;
import org.example.msaccountreservation.account.AccountStatusEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountStatusRepository extends JpaRepository<AccountStatus, Integer> {
    Optional<AccountStatus> findByName(AccountStatusEnum name);
}
