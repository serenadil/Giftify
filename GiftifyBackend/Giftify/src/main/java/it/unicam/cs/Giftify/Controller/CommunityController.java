package it.unicam.cs.Giftify.Controller;

import it.unicam.cs.Giftify.Model.DTOClass.CommunityCreateDTO;
import it.unicam.cs.Giftify.Model.DTOClass.CommunityUpdateDTO;
import it.unicam.cs.Giftify.Model.Entity.Account;
import it.unicam.cs.Giftify.Model.Entity.Community;
import it.unicam.cs.Giftify.Model.Services.CommunityService;
import it.unicam.cs.Giftify.Model.Util.AccessCodeGeneretor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class CommunityController {

    @Autowired
    private CommunityService communityService;

    @PostMapping("/create")
    public ResponseEntity<String> createCommunity(@RequestBody CommunityCreateDTO communityDto) {
        try {
            Account admin = communityService.findAccountById(communityDto.getAdminId());
            if (admin == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Admin non trovato.");
            }
            communityService.createCommunity(
                    new AccessCodeGeneretor(),
                    admin,
                    communityDto.getCommunityName(),
                    communityDto.getDescription(),
                    communityDto.getNote(),
                    communityDto.getBudget(),
                    communityDto.getDeadline()
            );
            return ResponseEntity.ok("Community creata con successo.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    @PostMapping("/close/{id}")
    public ResponseEntity<String> closeCommunity(@PathVariable long id) {
        Community community = communityService.getCommunityById(id);
        if (community != null) {
            communityService.drawNames(community);
            return ResponseEntity.ok("Community chiusa con successo.");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Community non trovata.");
    }

    @PostMapping("/delete/{id}")
    public ResponseEntity<String> deleteCommunity(@PathVariable long id) {
        Community community = communityService.getCommunityById(id);
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
        Community community = communityService.getCommunityById(id);
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

    @GetMapping("/roles/{id}")
    public ResponseEntity<Map<Account, String>> getRoles(@PathVariable long id) {
        Community community = communityService.getCommunityById(id);
        if (community != null) {
            Map<Account, String> roles = new HashMap<>();
            roles.put(community.getAdmin(), "Admin");
            for (Account user : community.getUserList()) {
                roles.putIfAbsent(user, "Member");
            }
            return ResponseEntity.ok(roles);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    @GetMapping("/participants/{id}")
    public ResponseEntity<List<Account>> getParticipants(@PathVariable long id) {
        Community community = communityService.getCommunityById(id);
        if (community != null) {
            return ResponseEntity.ok(community.getUserList());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    @GetMapping("/info/{id}")
    public ResponseEntity<Community> getGeneralInfo(@PathVariable long id) {
        Community community = communityService.getCommunityById(id);
        if (community != null) {
            return ResponseEntity.ok(community);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    @GetMapping("/draw/{id}")
    public ResponseEntity<Account> viewDrawnName(@PathVariable long id, @RequestParam long userId) {
        Community community = communityService.getCommunityById(id);
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
