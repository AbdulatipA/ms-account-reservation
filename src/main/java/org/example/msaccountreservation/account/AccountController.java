package org.example.msaccountreservation.account;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@RestController
@RequestMapping("${api.prefix}")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;

    @PostMapping("/account/{clientId}")
    public ResponseEntity<AccountResponseDTO> create(@RequestBody AccountRequestDTO accountRequestDTO, @PathVariable UUID clientId) {
        return ResponseEntity.ok(accountService.createAccount(accountRequestDTO, clientId));
    }

    @GetMapping("/account/{accountId}")
    public ResponseEntity<AccountResponseDTO> create(@PathVariable UUID accountId) {
        return ResponseEntity.ok(accountService.getAccount(accountId));
    }
}
