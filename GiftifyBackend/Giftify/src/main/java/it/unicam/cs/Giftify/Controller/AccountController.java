package it.unicam.cs.Giftify.Controller;

import it.unicam.cs.Giftify.Model.Entity.Account;
import it.unicam.cs.Giftify.Model.Entity.Community;
import it.unicam.cs.Giftify.Model.Services.AccountService;
import it.unicam.cs.Giftify.Model.Services.CommunityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
public class AccountController {
    @Autowired
    private AccountService accountService;
    @Autowired
    private CommunityService communityService;


    @GetMapping("/accountInfo")
    public ResponseEntity<?> getAccountInfo() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Account account = (Account) authentication.getPrincipal();
            account = accountService.getAccountById(account.getId());
            if (account == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            return ResponseEntity.ok(account);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ops sembra ci sia stato un errore!" + e.getMessage());
        }

    }

    @GetMapping("/getCommunities")
    public ResponseEntity<?> getAllCommunities() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            Account account = (Account) authentication.getPrincipal();
            account = accountService.getAccountById(account.getId());
            if (account == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            return ResponseEntity.ok(account.getUserCommunities());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ops sembra ci sia stato un errore!" + e.getMessage());
        }
    }

    @PostMapping("/join/{communityAccessCode}")
    public ResponseEntity<String> joinCommunity(@PathVariable String communityAccessCode) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Account account = (Account) authentication.getPrincipal();
            account = accountService.getAccountById(account.getId());
            Community community = communityService.getCommunityByAccessCode(communityAccessCode);
            if (community == null || account == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            communityService.addUserToCommunity(account, community);
            return ResponseEntity.ok("Unione alla community effettuata con successo");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ops sembra ci sia stato un errore!" + e.getMessage());
        }
    }
}
