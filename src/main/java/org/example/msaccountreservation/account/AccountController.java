package org.example.msaccountreservation.account;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@RestController
@RequestMapping("/account")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;

    @PostMapping("/{clientId}")
    public ResponseEntity<AccountResponseDTO> create(@RequestBody AccountRequestDTO accountRequestDTO, @PathVariable UUID clientId) {
        return ResponseEntity.ok(accountService.createAccount(accountRequestDTO, clientId));
    }

    @GetMapping("/{accountId}")
    public ResponseEntity<AccountResponseDTO> create(@PathVariable UUID accountId) {
        return ResponseEntity.ok(accountService.getAccount(accountId));
    }
}
