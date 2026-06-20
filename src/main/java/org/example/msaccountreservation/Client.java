package org.example.msaccountreservation;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "client")
public class Client {
    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "full_name", nullable = false, length = 50)
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


    public Client() {
    }

    public Client(UUID id, String fullName, String citizenship, String clientType, String documentNumber, String documentSeries, String documentType, long mdmCode) {
        this.id = id;
        this.fullName = fullName;
        this.citizenship = citizenship;
        this.clientType = clientType;
        this.documentNumber = documentNumber;
        this.documentSeries = documentSeries;
        this.documentType = documentType;
        this.mdmCode = mdmCode;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setCitizenship(String citizenship) {
        this.citizenship = citizenship;
    }

    public void setClientType(String clientType) {
        this.clientType = clientType;
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    public void setDocumentSeries(String documentSeries) {
        this.documentSeries = documentSeries;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public void setMdmCode(long mdmCode) {
        this.mdmCode = mdmCode;
    }

    public UUID getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public String getCitizenship() {
        return citizenship;
    }

    public String getClientType() {
        return clientType;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public String getDocumentSeries() {
        return documentSeries;
    }

    public String getDocumentType() {
        return documentType;
    }

    public long getMdmCode() {
        return mdmCode;
    }
}