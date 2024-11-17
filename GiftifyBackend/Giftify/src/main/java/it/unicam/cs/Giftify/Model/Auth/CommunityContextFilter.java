package it.unicam.cs.Giftify.Model.Auth;

import it.unicam.cs.Giftify.Model.Entity.Account;
import it.unicam.cs.Giftify.Model.Entity.Community;
import it.unicam.cs.Giftify.Model.Entity.Role;
import it.unicam.cs.Giftify.Model.Services.AccountService;
import it.unicam.cs.Giftify.Model.Services.CommunityService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class CommunityContextFilter extends OncePerRequestFilter {

    @Autowired
    private CommunityService communityService;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String communityId = request.getParameter("communityId");

        if (communityId != null) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null) {
                Account account = (Account) authentication.getPrincipal();
                Community community = communityService.getCommunityById(Long.parseLong(communityId));

                Role role = account.getRoleForCommunity(community);
                if (role != null) {
                    List<GrantedAuthority> authorities = new ArrayList<>(authentication.getAuthorities());
                    authorities.add(new SimpleGrantedAuthority("ROLE_" + role.name() + "_" + community.getId()));
                    Authentication newAuth = new UsernamePasswordAuthenticationToken(account, authentication.getCredentials(), authorities);
                    SecurityContextHolder.getContext().setAuthentication(newAuth);
                }
            }
        }

        filterChain.doFilter(request, response);
    }


}
