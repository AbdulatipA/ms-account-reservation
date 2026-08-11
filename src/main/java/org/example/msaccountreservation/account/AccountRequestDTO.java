package org.example.msaccountreservation.account;

import lombok.Data;

@Data
public class AccountRequestDTO {
    private String accountType;
    private String currencyCode;
}
