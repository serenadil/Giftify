package it.unicam.cs.Giftify.Model.Services;

import it.unicam.cs.Giftify.Model.Auth.AuthResponse;
import it.unicam.cs.Giftify.Model.Auth.ExistingUserException;
import it.unicam.cs.Giftify.Model.Auth.LoginRequest;
import it.unicam.cs.Giftify.Model.Auth.RegisterRequest;
import it.unicam.cs.Giftify.Model.Entity.Account;
import it.unicam.cs.Giftify.Model.Repository.AccountRepository;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccountServices {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private JwtService jwtService;


    public AuthResponse register(RegisterRequest request) throws ExistingUserException {
        if (accountRepository.existsAccountByEmail(request.email())) throw new ExistingUserException();
        Account user = new Account();
        accountRepository.save(user);
        return new AuthResponse(jwtService.generateToken(user));
    }

    public AuthResponse login(LoginRequest request) {
        var user = accountRepository.findByEmail(request.email()).orElseThrow();
        return new AuthResponse(jwtService.generateToken(user));
    }


    public void saveAccount(@NonNull Account account) {
        accountRepository.save(account);
    }


    public Optional<Account> getAccount(String email) {
        return accountRepository.findByEmail(email);
    }
}
