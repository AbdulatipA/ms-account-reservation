package org.example.msaccountreservation;

import jakarta.persistence.*;

@Entity
@Table(name = "account_status")
public class AccountStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name", nullable = false, length = 30)
    @Enumerated(EnumType.STRING)
    private StatusName name;

    @Column(name = "description", nullable = false)
    private String description;


    public AccountStatus() {
    }

    public AccountStatus(int id, StatusName name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public StatusName getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(StatusName name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}