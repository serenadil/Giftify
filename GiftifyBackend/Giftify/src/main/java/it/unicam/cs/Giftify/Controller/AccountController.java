package it.unicam.cs.Giftify.Controller;

import it.unicam.cs.Giftify.Model.Entity.Account;
import it.unicam.cs.Giftify.Model.Entity.Community;
import it.unicam.cs.Giftify.Model.Services.AccountService;
import it.unicam.cs.Giftify.Model.Services.CommunityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/account")
public class AccountController {
    @Autowired
    private AccountService accountService;
    @Autowired
    private CommunityService communityService;

    @GetMapping("/getgroups")
    public ResponseEntity<List<Community>> getAllCommunities(@PathVariable Long userId) {
        Account account = accountService.getAccountById(userId);
        if (account == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(communityService.getAllCommunities());
    }
}
