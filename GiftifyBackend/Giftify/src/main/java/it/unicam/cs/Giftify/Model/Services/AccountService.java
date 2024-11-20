package it.unicam.cs.Giftify.Model.Services;

import it.unicam.cs.Giftify.Model.Auth.ExistingUserException;
import it.unicam.cs.Giftify.Model.Entity.Account;
import it.unicam.cs.Giftify.Model.Entity.Community;
import it.unicam.cs.Giftify.Model.Entity.Role;
import it.unicam.cs.Giftify.Model.Repository.AccountRepository;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;


    public Account createAccount(String email, String password) throws ExistingUserException {
        Optional<Account> account = accountRepository.findByEmail(email);
        if (account.isPresent()) {
            throw new ExistingUserException();
        }
        Account newAccount = new Account(email, password);
        accountRepository.save(newAccount);
        return newAccount;
    }


    public void saveAccount(@NonNull Account account) {
        accountRepository.save(account);
    }


    public Optional<Account> getAccount(String email) {
        return accountRepository.findByEmail(email);
    }

    public Account getAccountById(Long id) {
        return accountRepository.findById(id).orElseThrow();
    }


}
