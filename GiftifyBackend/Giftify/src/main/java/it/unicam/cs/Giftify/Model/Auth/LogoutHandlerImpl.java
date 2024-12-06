package it.unicam.cs.Giftify.Model.Auth;


import it.unicam.cs.Giftify.Model.Entity.RevokedToken;
import it.unicam.cs.Giftify.Model.Entity.Token;
import it.unicam.cs.Giftify.Model.Repository.RevokedTokenRepository;
import it.unicam.cs.Giftify.Model.Repository.TokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import it.unicam.cs.Giftify.Model.Entity.Token;

import java.util.Date;

@Configuration
public class LogoutHandlerImpl implements LogoutHandler {

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private RevokedTokenRepository revokedTokenRepository;


    @Override
    public void logout(HttpServletRequest request,
                       HttpServletResponse response,
                       Authentication authentication) {
        String authHeader = request.getHeader("Authorization");

        if(authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }

        String token = authHeader.substring(7);

        RevokedToken revokedToken = new RevokedToken();
        revokedToken.setToken(token);
        revokedToken.setTokenType(RevokedToken.TokenType.ACCESS);
        revokedToken.setRevokedAt(new Date());

        revokedTokenRepository.save(revokedToken);
        Token storedToken = tokenRepository.findByAccessToken(token).orElse(null);

        if(storedToken != null) {
            storedToken.setLoggedOut(true);
            tokenRepository.save(storedToken);
        }
    }


}
