package it.unicam.cs.Giftify.Model.Services;

import it.unicam.cs.Giftify.Model.Entity.Account;
import it.unicam.cs.Giftify.Model.Repository.AccountRepository;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountServices {

    @Autowired
    private AccountRepository accountRepository;


    public void saveAccount( @NonNull Account account) {
        accountRepository.save(account);
    }
}
