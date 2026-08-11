package org.example.msaccountreservation.account;

import org.example.msaccountreservation.client.Client;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AccountRepository extends JpaRepository<Account, UUID> {

     @EntityGraph(attributePaths = {"status"})
     List<Account> findByClient(Client client);

     @Query("SELECT a FROM Account a JOIN FETCH a.status JOIN FETCH a.client WHERE a.id = :id")
     Optional<Account> findById(@Param("id") UUID id);

     long countByClientAndStatusName(Client client, AccountStatusEnum statusName);
}
