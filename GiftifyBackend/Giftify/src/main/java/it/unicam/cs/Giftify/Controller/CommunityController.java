package it.unicam.cs.Giftify.Controller;

import it.unicam.cs.Giftify.Model.Entity.Account;
import it.unicam.cs.Giftify.Model.Entity.Community;
import it.unicam.cs.Giftify.Model.Entity.Role;
import it.unicam.cs.Giftify.Model.Services.AccountService;
import it.unicam.cs.Giftify.Model.Services.CommunityService;
import it.unicam.cs.Giftify.Model.Util.AccessCodeGeneretor;
import it.unicam.cs.Giftify.Model.Util.DTOClasses.CommunityCreateDTO;
import it.unicam.cs.Giftify.Model.Util.DTOClasses.CommunityUpdateDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController("/community")
public class CommunityController {

    @Autowired
    private CommunityService communityService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private AccessCodeGeneretor accessCodeGeneretor;

    @PostMapping("/create")
    public ResponseEntity<String> createCommunity(@RequestBody CommunityCreateDTO communityDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Account admin = (Account) authentication.getPrincipal();
        admin = accountService.getAccountById(admin.getId());
        communityService.createCommunity(
                accessCodeGeneretor,
                admin,
                communityDto.getCommunityName(),
                communityDto.getNote(),
                communityDto.getBudget(),
                communityDto.getDeadline()
        );
        return ResponseEntity.ok("Community creata con successo.");
    }


    @DeleteMapping("/removeUser/{id}")
    public ResponseEntity<Void> removeUserFromCommunity(@PathVariable Long communityId, @RequestBody Account user) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Account admin = (Account) authentication.getPrincipal();
        admin = accountService.getAccountById(admin.getId());
        Community community = communityService.getCommunityById(communityId);
        if (!admin.getRoleForCommunity(community).equals(Role.ADMIN)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        if (admin == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        communityService.removeUserFromCommunity(user, community);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


    @PostMapping("/close/{id}")
    public ResponseEntity<String> closeCommunity(@PathVariable long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Account admin = (Account) authentication.getPrincipal();
        admin = accountService.getAccountById(admin.getId());
        Community community = communityService.getCommunityById(id);
        if (!admin.getRoleForCommunity(community).equals(Role.ADMIN)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        if (community != null) {
            communityService.drawNames(community);
            return ResponseEntity.ok("Community chiusa con successo.");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Community non trovata.");
    }

    @PostMapping("/delete/{id}")
    public ResponseEntity<String> deleteCommunity(@PathVariable long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Account admin = (Account) authentication.getPrincipal();
        admin = accountService.getAccountById(admin.getId());
        Community community = communityService.getCommunityById(id);
        if (!admin.getRoleForCommunity(community).equals(Role.ADMIN)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        if (community != null) {
            communityService.deleteGroup(community);
            return ResponseEntity.ok("Community eliminata con successo.");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Community non trovata.");
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateCommunity(
            @PathVariable long id,
            @RequestBody CommunityUpdateDTO communityUpdateDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Account admin = (Account) authentication.getPrincipal();
        admin = accountService.getAccountById(admin.getId());
        Community community = communityService.getCommunityById(id);
        if (!admin.getRoleForCommunity(community).equals(Role.ADMIN)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        if (community != null) {
            if (communityUpdateDto.getCommunityName() != null) {
                community.setCommunityName(communityUpdateDto.getCommunityName());
            }
            if (communityUpdateDto.getBudget() != null) {
                community.setBudget(communityUpdateDto.getBudget());
            }
            if (communityUpdateDto.getNote() != null) {
                community.setCommunityNote(communityUpdateDto.getNote());
            }
            communityService.updateCommunity(community);
            return ResponseEntity.ok("Community aggiornata con successo.");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Community non trovata.");
    }


    @GetMapping("/participants/{id}")
    public ResponseEntity<List<Account>> getParticipants(@PathVariable long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Account account = (Account) authentication.getPrincipal();
        account = accountService.getAccountById(account.getId());
        Community community = communityService.getCommunityById(id);
        if (account.getRoleForCommunity(community) == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        if (community != null) {
            return ResponseEntity.ok(community.getUserList());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    @GetMapping("/info/{id}")
    public ResponseEntity<Community> getGeneralInfo(@PathVariable long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Account account = (Account) authentication.getPrincipal();
        account = accountService.getAccountById(account.getId());
        Community community = communityService.getCommunityById(id);
        if (account.getRoleForCommunity(community) == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        if (community != null) {
            return ResponseEntity.ok(community);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    @GetMapping("/draw/{id}")
    public ResponseEntity<Account> viewDrawnName(@PathVariable long id, @RequestParam long userId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Account admin = (Account) authentication.getPrincipal();
        admin = accountService.getAccountById(admin.getId());
        Community community = communityService.getCommunityById(id);
        if (!admin.getRoleForCommunity(community).equals(Role.ADMIN)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        if (community != null) {
            Account user = community.getUserList().stream()
                    .filter(u -> u.getId().equals(userId))
                    .findFirst()
                    .orElse(null);
            if (user != null) {
                Account receiver = community.getGiftReceiver(user);
                return ResponseEntity.ok(receiver);
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

}
