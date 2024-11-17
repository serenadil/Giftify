package it.unicam.cs.Giftify.Model.Services;

import it.unicam.cs.Giftify.Model.Entity.Account;
import it.unicam.cs.Giftify.Model.Entity.Community;
import it.unicam.cs.Giftify.Model.Entity.WishList;
import it.unicam.cs.Giftify.Model.Repository.CommunityRepository;
import it.unicam.cs.Giftify.Model.Util.AccessCodeGeneretor;
import jakarta.transaction.Transactional;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class CommunityService {

    @Autowired
    private CommunityRepository communityRepository;

    @Autowired
    private AccountService accountServices;

    @Autowired
    private WishListService wishListService;
    @Transactional
    public void createCommunity(AccessCodeGeneretor codeGeneretor, Account admin, String name,
                                String note, double budget, LocalDate deadline) {
        Community community = new Community(codeGeneretor, admin, name, note, budget, deadline);
        WishList wishList=wishListService.createWishList(admin);
        community.addUser(admin, wishList);
        communityRepository.save(community);
        admin.addCommunity(community);
        accountServices.saveAccount(admin);
    }


    public void deleteGroup(@NonNull Community Community) {
        communityRepository.delete(Community);
    }


    @Scheduled(cron = "0 0 0 * * ?")
    public void checkDeadline() {
        List<Community> activeGroups = communityRepository.findByActive(true);
        for (Community community : activeGroups) {
            if (community.getDeadline().isBefore(LocalDate.now())) {
                community.setActive(false);
                communityRepository.save(community);
            }
        }

    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void deleteGroups() {
        List<Community> inactiveGroups = communityRepository.findByActive(false);
        LocalDate currentDate = LocalDate.now();
        for (Community community : inactiveGroups) {
            if (currentDate.isAfter(community.getDeadline().plusDays(30))) {
                this.deleteGroup(community);
                List<Account> users = community.getUserList();
                for (Account account : users) {
                    account.removeCommunity(community);
                    accountServices.saveAccount(account);
                }
            }
        }
    }


    public Community getCommunityById(long id) {
        return communityRepository.findById(id).orElse(null);
    }

    public List<Community> getAllCommunities() {
        return communityRepository.findAll();
    }


    public Community getCommunityByAccessCode(@NonNull String accessCode) {
        return communityRepository.findByAccessCode(accessCode).orElse(null);
    }

    public void updateCommunity(Community community) {
        communityRepository.save(community);
    }

    public void addUserToCommunity(@NonNull Account user, @NonNull Community community) {
        if (!community.getUserList().contains(user)) {
            community.addUser(user, wishListService.createWishList(user));
            user.addCommunity(community);
            accountServices.saveAccount(user);
            communityRepository.save(community);
        } else throw new IllegalArgumentException("Sei già iscritto a questo gruppo!");
    }

    public void removeUserFromCommunity( @NonNull Account user, @NonNull Community community) {
        if (community.getUserList().contains(user)) {
            community.removeUser(user);
            user.removeCommunity(community);
            accountServices.saveAccount(user);
            communityRepository.save(community);
        } else throw new IllegalArgumentException("Impossibile rimuovere, utente non presente.");
    }

    public void drawNames(@NonNull Community community) {
        if (community.getUserList().size() % 2 != 0) {
            throw new IllegalArgumentException("Il numero di partecipanti deve essere pari per effettuare l'estrazione.");
        }
        Map<Account, Account> accountMap = new HashMap<>();
        List<Account> shuffledUsers = new ArrayList<>(community.getUserList());
        Collections.shuffle(shuffledUsers);
        for (int i = 0; i < shuffledUsers.size(); i++) {
            Account giver = shuffledUsers.get(i);
            Account receiver = shuffledUsers.get((i + 1) % shuffledUsers.size());
            accountMap.put(giver, receiver);
        }
        community.setGiftAssignments(accountMap);
        community.setClose(true);
    }


    public Account findAccountById(Long id) {
        return accountServices.getAccountById(id);
    }
    public boolean isUserParticipantOfCommunity(long communityId, Object principal) {
        Account account = (Account) principal;
        Community community = getCommunityById(communityId);
        return community != null && community.getUserList().contains(account);
    }

}
