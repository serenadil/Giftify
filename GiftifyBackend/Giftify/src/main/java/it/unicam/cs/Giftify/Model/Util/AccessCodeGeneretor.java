package it.unicam.cs.Giftify.Model.Util;

import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
@Component
public class AccessCodeGeneretor {
    private final Set<String> generatedCode;

    public AccessCodeGeneretor() {
        generatedCode = new HashSet<>();
    }

    public String generateCode() {
        String newCode;
        do {
            newCode = UUID.randomUUID().toString();
        } while (generatedCode.contains(newCode));
        generatedCode.add(newCode);
        return newCode;
    }

    public void removeCode(String code) {
        generatedCode.remove(code);
    }

    public boolean verifyCode(String code) {
        return generatedCode.contains(code);
    }
}
