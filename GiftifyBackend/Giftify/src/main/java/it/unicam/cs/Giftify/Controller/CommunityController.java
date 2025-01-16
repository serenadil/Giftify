package it.unicam.cs.Giftify.Controller;

import it.unicam.cs.Giftify.Model.Entity.Account;
import it.unicam.cs.Giftify.Model.Entity.Community;
import it.unicam.cs.Giftify.Model.Entity.Role;
import it.unicam.cs.Giftify.Model.Entity.WishList;
import it.unicam.cs.Giftify.Model.Repository.CommunityRepository;
import it.unicam.cs.Giftify.Model.Services.AccountService;
import it.unicam.cs.Giftify.Model.Services.CommunityService;
import it.unicam.cs.Giftify.Model.Services.WishListService;
import it.unicam.cs.Giftify.Model.Util.DTOClasses.CommunityCreateDTO;
import it.unicam.cs.Giftify.Model.Util.DTOClasses.CommunityUpdateDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@RestController
public class CommunityController {

    @Autowired
    private CommunityService communityService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private CommunityRepository communityRepository;
    private WishListService wishListService;

    @GetMapping("community/role/{communityId}")
    public ResponseEntity<?> getRoleForCommunity(@PathVariable UUID communityId) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Account account = (Account) authentication.getPrincipal();
            account = accountService.getAccountById(account.getId());
            if (account == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            Community community = communityService.getCommunityById(communityId);
            return ResponseEntity.ok(account.getRoleForCommunity(community));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ops sembra ci sia stato un errore!");
        }
    }

    @PostMapping("/community/createCommunity")
    public ResponseEntity<String> createCommunity(@RequestBody CommunityCreateDTO communityDto) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Account admin = (Account) authentication.getPrincipal();
            System.out.println("Utente autenticato: " + admin.getEmail());
            admin = accountService.getAccountById(admin.getId());
            communityService.createCommunity(
                    admin,
                    communityDto.getCommunityName(),
                    communityDto.getCommunityNote(),
                    communityDto.getBudget(),
                    communityDto.getDeadline(),
                    communityDto.getUserCommunityName()
            );
            System.out.println(communityRepository.findByActive(true).get(0).getCommunityName());
            System.out.println(communityRepository.findByActive(true).get(0).getCommunityNote());
            System.out.println(communityRepository.findByActive(true).get(0).getBudget());
            System.out.println(communityRepository.findByActive(true).get(0).getDeadline());
            System.out.println(communityRepository.findByActive(true).get(0).getCommunityNames().stream().toList().get(0).getUserCommunityName());


            return ResponseEntity.ok("Community creata con successo.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ops sembra ci sia stato un errore!");
        }
    }


    @DeleteMapping("/community/removeUser/{communityId}/{userId}")
    public ResponseEntity<String> removeUserFromCommunity(@PathVariable UUID communityId, @PathVariable Long userId) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Account admin = (Account) authentication.getPrincipal();
            admin = accountService.getAccountById(admin.getId());
            Community community = communityService.getCommunityById(communityId);
            if (!admin.getRoleForCommunity(community).equals(Role.ADMIN)) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN);
            }
            Account user = accountService.getAccountById(userId);
            communityService.removeUserFromCommunity(user, community);
            return ResponseEntity.ok("Abbiamo rimosso l'utente selezionato");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ops sembra ci sia stato un errore!");
        }
    }


    @PostMapping("/community/closeCommunity/{id}")
    public ResponseEntity<String> closeCommunity(@PathVariable UUID id) {
        try {

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
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ops sembra ci sia stato un errore");
        }
    }

    @DeleteMapping("/community/deleteCommunity/{id}")
    public ResponseEntity<String> deleteCommunity(@PathVariable UUID id) {
        try {
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

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ops sembra ci sia stato un errore!");
        }
    }

    @PutMapping("/community/updateCommunity/{id}")
    public ResponseEntity<String> updateCommunity(
            @PathVariable UUID id,
            @RequestBody CommunityUpdateDTO communityUpdateDto) {
        try {
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
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ops sembra ci sia stato un errore!");
        }
    }


    @GetMapping("/community/participants/{id}")
    public ResponseEntity<?> getParticipants(@PathVariable UUID id) {
        try {
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
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ops sembra ci sia stato un errore!");
        }
    }

    @GetMapping("/community/infoCommunity/{id}")
    public ResponseEntity<?> getGeneralInfo(@PathVariable UUID id) {
        System.out.println(id);
        try {
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
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ops sembra ci sia stato un errore!");
        }
    }

    @GetMapping("/community/drawnName/{id}")
    public ResponseEntity<String> viewDrawnName(@PathVariable UUID id) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Account account = (Account) authentication.getPrincipal();
            account = accountService.getAccountById(account.getId());
            String userReceiverCommName= communityService.getReceiverName(account, id);
            return ResponseEntity.ok(userReceiverCommName);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ops sembra ci sia stato un errore!");
        }
    }


//    @GetMapping("/community/drawnNameList/{id}")
//    public ResponseEntity<?> viewDrawnNameList(@PathVariable UUID id) {
//        try {
//            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//            Account account = (Account) authentication.getPrincipal();
//            account = accountService.getAccountById(account.getId());
//            Community community = communityService.getCommunityById(id);
//            if (community == null || account.getRoleForCommunity(community) != Role.MEMBER) {
//                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
//            }
//            Optional<Account> optionalReceiver = accountService.getAccount(community.getGiftReceiver(account));
//            if (optionalReceiver.isEmpty()) {
//                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
//            }
//            WishList wishList = community.getuserWishList(optionalReceiver.get());
//            return ResponseEntity.ok(wishList);
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ops sembra ci sia stato un errore!" );
//        }
//    }

    @GetMapping("/community/{communityId}/participantList/{accountCommunityName}")
    public ResponseEntity<?> viewParticipantList(@PathVariable UUID communityId, @PathVariable String accountCommunityName) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Account account = (Account) authentication.getPrincipal();
            account = accountService.getAccountById(account.getId());
            Community community = communityService.getCommunityById(communityId);
            if (community == null || account.getRoleForCommunity(community) == null) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN);
            }
            WishList wishList = community.getuserWishList(community.getCommunityNameByAccount(account));
            if (wishList == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Wishlist non trovata per il partecipante.");
            }
            return ResponseEntity.ok(wishList);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore interno del server: " + e.getMessage());
        }
    }

    @GetMapping("community/{communityId}/myWishlist")
    public ResponseEntity<?> getWishList(@PathVariable UUID communityId) {
        try {
            System.out.println(communityId);
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Account user = (Account) authentication.getPrincipal();
            user = accountService.getAccountById(user.getId());
            System.out.println(user.getEmail());
            Community community = communityService.getCommunityById(communityId);
            if (user.getRoleForCommunity(community) == null) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN);
            }
            System.out.println(user.getRoleForCommunity(community));
            String userCommunityName= community.getCommunityNameByAccount(user);
            System.out.println(userCommunityName);
            WishList wishList = community.getuserWishList(userCommunityName);
            System.out.println(wishList.getWishes().stream().toList().get(0).getName());
            return ResponseEntity.ok(wishList);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ops sembra ci sia stato un errore!");
        }
    }

    @GetMapping("/community/wishlists/{id}")
    public ResponseEntity<?> getWishlists(@PathVariable UUID id) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Account account = (Account) authentication.getPrincipal();
            account = accountService.getAccountById(account.getId());
            Community community = communityService.getCommunityById(id);
            if (account.getRoleForCommunity(community) == null) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN);
            }
            if (community != null) {
                Set<WishList> wishlists = community.getWishlists();
                return ResponseEntity.ok(wishlists);
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ops sembra ci sia stato un errore!");
        }
    }


}
