package org.example.msaccountreservation;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "client")
public class Client {
    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(name = "citizenship", nullable = false, length = 50)
    private String citizenship;

    @Column(name = "client_type", nullable = false, length = 30)
    private String clientType;

    @Column(name = "document_number", nullable = false, length = 30)
    private String documentNumber;

    @Column(name = "document_series", nullable = false, length = 30)
    private String documentSeries;

    @Column(name = "document_type", nullable = false, length = 30)
    private String documentType;

    @Column(name = "mdm_code", nullable = false)
    private long mdmCode;
}