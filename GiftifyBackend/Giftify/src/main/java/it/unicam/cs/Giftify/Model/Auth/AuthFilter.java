package it.unicam.cs.Giftify.Model.Auth;

import it.unicam.cs.Giftify.Model.Services.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
/**
 * Filtro personalizzato per l'autenticazione JWT.
 * Intercetta le richieste e autentica l'utente in base al token JWT presente nell'intestazione.
 */
@Component
public class AuthFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserDetailsService userDetailsService;
    /**
     * Esegue il filtro per autenticare l'utente utilizzando il token JWT.
     * Salta la logica di autenticazione per endpoint specifici come `/register` e `/login`.
     *
     * @param request     la richiesta HTTP.
     * @param response    la risposta HTTP.
     * @param filterChain la catena di filtri di Spring Security.
     * @throws ServletException in caso di errore del servlet.
     * @throws IOException      in caso di errore di input/output.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // Ottieni l'URL dell'endpoint richiesto
        String requestURI = request.getRequestURI();

        // Se l'endpoint è /register o /login, salta la logica di autenticazione
        if (requestURI.equals("/register") || requestURI.equals("/login")) {
            filterChain.doFilter(request, response);
            return;  // Non fare nulla e lascia che la richiesta continui
        }

        final String authHeader = request.getHeader("Authorization");
        final String token;
        final String username;

        // Se non c'è l'intestazione Authorization o il token non inizia con "Bearer ", passa al prossimo filtro
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Estrai il token
        token = authHeader.substring(7);  // Rimuove "Bearer " dal prefisso
        username = jwtService.extractUsername(token);

        // Se il nome utente è presente e non c'è un'autenticazione già nel contesto di sicurezza
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

            // Se il token è valido, imposta l'autenticazione nel SecurityContext

            if (jwtService.isTokenValid(token, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                );


                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // Continua con la catena di filtri
        filterChain.doFilter(request, response);
    }

}

