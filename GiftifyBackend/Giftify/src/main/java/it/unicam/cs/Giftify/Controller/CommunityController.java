package it.unicam.cs.Giftify.Controller;

import it.unicam.cs.Giftify.Model.Entity.Account;
import it.unicam.cs.Giftify.Model.Entity.Community;
import it.unicam.cs.Giftify.Model.Entity.Role;
import it.unicam.cs.Giftify.Model.Entity.WishList;
import it.unicam.cs.Giftify.Model.Services.AccountService;
import it.unicam.cs.Giftify.Model.Services.CommunityService;
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
import java.util.Map;

@RestController
public class CommunityController {

    @Autowired
    private CommunityService communityService;

    @Autowired
    private AccountService accountService;


    @PostMapping("/createCommunity")
    public ResponseEntity<String> createCommunity(@RequestBody CommunityCreateDTO communityDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Account admin = (Account) authentication.getPrincipal();
        admin = accountService.getAccountById(admin.getId());
        communityService.createCommunity(
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

    @PostMapping("/closeCommunity/{id}")
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

    @DeleteMapping("/deleteCommunity/{id}")
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

    @PutMapping("/updateCommunity/{id}")
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

    @GetMapping("/infoCommunity/{id}")
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

    @GetMapping("/drawedName/{id}")
    public ResponseEntity<String> viewDrawnName(@PathVariable long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Account account = (Account) authentication.getPrincipal();
        account = accountService.getAccountById(account.getId());
        Community community = communityService.getCommunityById(id);
        if (community != null && account.getRoleForCommunity(community) == Role.MEMBER) {
            if (account != null) {
                Account receiver = community.getGiftReceiver(account);
                return ResponseEntity.ok(receiver.getUsername());
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }


    @GetMapping("/drawedNameList/{id}")
    public ResponseEntity<WishList> viewDrawnNameList(@PathVariable long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Account account = (Account) authentication.getPrincipal();
        account = accountService.getAccountById(account.getId());
        Community community = communityService.getCommunityById(id);
        if (community != null && account.getRoleForCommunity(community) == Role.MEMBER) {
            if (account != null) {
                Account receiver = community.getGiftReceiver(account);
                WishList wishList = community.getuserWishList(receiver);
                return ResponseEntity.ok(wishList);
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }


    @GetMapping("/wishlists/{id}")
    public ResponseEntity<Map<Account, WishList>> getWishlists(@PathVariable long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Account account = (Account) authentication.getPrincipal();
        account = accountService.getAccountById(account.getId());
        Community community = communityService.getCommunityById(id);
        if (account.getRoleForCommunity(community) == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        if (community != null) {
            Map<Account, WishList> wishlists = community.getWishlists();
            return ResponseEntity.ok(wishlists);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }


}
