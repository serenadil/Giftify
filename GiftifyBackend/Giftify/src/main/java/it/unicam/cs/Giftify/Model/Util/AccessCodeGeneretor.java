package it.unicam.cs.Giftify.Model.Util;

import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
/**
 * Classe per la gestione dei codici di accesso unici generati per le community.
 */
@Component
public class AccessCodeGeneretor {
    private final Set<String> generatedCode;

    /**
     * Costruttore che inizializza il set dei codici generati.
     */
    public AccessCodeGeneretor() {
        generatedCode = new HashSet<>();
    }

    /**
     * Genera un nuovo codice di accesso unico.
     *
     * @return il codice di accesso generato
     */
    public String generateCode() {
        String newCode;
        do {
            newCode = UUID.randomUUID().toString().substring(0, 8);
        } while (generatedCode.contains(newCode));
        generatedCode.add(newCode);
        return newCode;
    }

    /**
     * Rimuove un codice di accesso generato.
     *
     * @param code il codice di accesso da rimuovere
     */
    public void removeCode(String code) {
        generatedCode.remove(code);
    }

    /**
     * Verifica se un codice di accesso esiste già.
     *
     * @param code il codice da verificare
     * @return true se il codice esiste, altrimenti false
     */
    public boolean verifyCode(String code) {
        return generatedCode.contains(code);
    }
}
