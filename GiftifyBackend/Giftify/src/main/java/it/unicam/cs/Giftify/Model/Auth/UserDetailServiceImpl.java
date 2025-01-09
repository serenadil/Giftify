package it.unicam.cs.Giftify.Model.Auth;

import it.unicam.cs.Giftify.Model.Services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
/**
 * Configurazione per il servizio di dettaglio dell'utente.
 * Fornisce un bean `UserDetailsService` che carica un account utilizzando il servizio `AccountService`.
 */
@Configuration
public class UserDetailServiceImpl {

    @Autowired
    private AccountService accountServices;

    /**
     * Configura il bean `UserDetailsService` per caricare i dettagli dell'utente
     * in base all'email.
     *
     * @return un'implementazione di `UserDetailsService`.
     */
    @Bean
    public UserDetailsService userDetailsService() {
        return email -> accountServices.getAccount(email)
                .orElseThrow(() -> new UsernameNotFoundException("Nessun account trovato per l'utente: " + email));
    }
}


