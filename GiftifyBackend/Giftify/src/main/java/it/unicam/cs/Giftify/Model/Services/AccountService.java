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

import java.util.NoSuchElementException;
import java.util.Optional;
/**
 * Servizio per gestire le operazioni sugli account, come la creazione e il recupero degli utenti.
 */
@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    /**
     * Crea un nuovo account se non esiste già un account con la stessa email.
     *
     * @param email l'email dell'account
     * @param password la password dell'account
     * @param username il nome utente dell'account
     * @return l'account creato
     * @throws ExistingUserException se esiste già un account con la stessa email
     */
    public Account createAccount(String email, String password, String username) throws ExistingUserException {
        Optional<Account> account = accountRepository.findByEmail(email);
        if (account.isPresent()) {
            throw new ExistingUserException();
        }
        Account newAccount = new Account(email, password, username);
        accountRepository.save(newAccount);
        return newAccount;
    }

    /**
     * Salva un account esistente nel database.
     *
     * @param account l'account da salvare
     */
    public void saveAccount(@NonNull Account account) {
        accountRepository.save(account);
    }

    /**
     * Recupera un account tramite l'email.
     *
     * @param email l'email dell'account
     * @return un oggetto Optional contenente l'account, se trovato
     */
    public Optional<Account> getAccount(String email) {
        return accountRepository.findByEmail(email);
    }

    /**
     * Recupera un account tramite l'ID.
     *
     * @param id l'ID dell'account
     * @return l'account trovato
     * @throws NoSuchElementException se l'account non viene trovato
     */
    public Account getAccountById(Long id) {
        return accountRepository.findById(id).orElseThrow();
    }
}
