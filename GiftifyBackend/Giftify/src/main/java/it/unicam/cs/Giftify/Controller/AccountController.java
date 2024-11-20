package it.unicam.cs.Giftify.Controller;

import it.unicam.cs.Giftify.Model.Entity.Account;
import it.unicam.cs.Giftify.Model.Entity.Community;
import it.unicam.cs.Giftify.Model.Services.AccountService;
import it.unicam.cs.Giftify.Model.Services.CommunityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/account")
public class AccountController {
    @Autowired
    private AccountService accountService;
    @Autowired
    private CommunityService communityService;

    @GetMapping("/getCommunities/{userId}")
    public ResponseEntity<List<Community>> getAllCommunities(@PathVariable Long userId) {
        Account account = accountService.getAccountById(userId);
        if (account == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(communityService.getAllCommunities());
    }

    @PostMapping("/join")
    public ResponseEntity<String> joinCommunity (@RequestParam Long userId, @RequestParam String communityId) {
        try {
            Account account = accountService.getAccountById(userId);
            Community community = communityService.getCommunityByAccessCode(communityId);
            if (community == null || account == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            communityService.addUserToCommunity(account, community);
            return ResponseEntity.ok("Unione alla community effettuata con successo");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
