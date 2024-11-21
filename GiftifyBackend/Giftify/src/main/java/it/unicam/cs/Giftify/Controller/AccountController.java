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

import java.util.List;

@RestController
@RequestMapping
public class AccountController {
    @Autowired
    private AccountService accountService;
    @Autowired
    private CommunityService communityService;



    @GetMapping("/accountInfo")
    public ResponseEntity<Account> getAccountInfo(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Account account = (Account) authentication.getPrincipal();
        account = accountService.getAccountById(account.getId());
        if (account == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(account);

    }

    @GetMapping("/getCommunities")
    public ResponseEntity<List<Community>> getAllCommunities() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Account account = (Account) authentication.getPrincipal();
        account = accountService.getAccountById(account.getId());
        if (account == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(communityService.getAllCommunities());
    }

    @PostMapping("/join/{communityAccessCode}")
    public ResponseEntity<String> joinCommunity(@PathVariable String communityAccessCode) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Account account = (Account) authentication.getPrincipal();
        account = accountService.getAccountById(account.getId());
        Community community = communityService.getCommunityByAccessCode(communityAccessCode);
        if (community == null || account == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        communityService.addUserToCommunity(account, community);
        return ResponseEntity.ok("Unione alla community effettuata con successo");
    }
}
