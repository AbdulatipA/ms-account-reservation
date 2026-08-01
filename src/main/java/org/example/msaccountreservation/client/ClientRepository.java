package org.example.msaccountreservation.client;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ClientRepository extends JpaRepository<Client, UUID>, JpaSpecificationExecutor<Client> {

    boolean existsByMdmCode(Long id);
    boolean existsByDocumentNumberAndDocumentSeries(String documentNumber, String series);
    Optional<Client> findByMdmCode(Long mdmCode);
}
