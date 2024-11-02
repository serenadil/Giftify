package it.unicam.cs.Giftify.Model.Services;

import it.unicam.cs.Giftify.Model.Entity.Community;
import it.unicam.cs.Giftify.Model.Entity.Account;
import it.unicam.cs.Giftify.Model.Repository.CommunityRepository;
import it.unicam.cs.Giftify.Model.Util.AccessCodeGeneretor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class CommunityServices {

    @Autowired
    private CommunityRepository communityRepository;

    public void createCommunity(AccessCodeGeneretor codeGeneretor, Account admin, String name,
                                String description, String note, double budget, LocalDate deadline) {
        Community community = new Community(codeGeneretor, admin, name, description, note, budget, deadline);
        communityRepository.save(community);
    }


    public void deleteGroup(Community Community) {
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
            }
        }
    }


    public Community getCommunityById(long id) {
        return communityRepository.findById(id).orElse(null);
    }


    public Community getCommunityByAccessCode(String accessCode) {
        return communityRepository.findByAccessCode(accessCode).orElse(null);
    }


    public void addUserToCommunity(Account user, Community community) {
        if (!community.getUserList().contains(user)) {
            community.addUser(user);
            communityRepository.save(community);
        } else throw new IllegalArgumentException("Sei già iscritto a questo gruppo!");
    }

    public void removeUserFromCommunity(Account user, Community community) {
        if (community.getUserList().contains(user)) {
            community.removeUser(user);
            communityRepository.save(community);
        } else throw new IllegalArgumentException("Impossibile rimuovere, utente non presente.");

    }

    public void drawNames(Community community) {
        if (community.getUserList().size() % 2 != 0) {
            throw new IllegalArgumentException("Il numero di partecipanti deve essere pari per effettuare l'estrazione.");
        }
        List<Account> shuffledUsers = new ArrayList<>(community.getUserList());
        Collections.shuffle(shuffledUsers);
        for (int i = 0; i < shuffledUsers.size(); i++) {
            Account giver = shuffledUsers.get(i);
            Account receiver = shuffledUsers.get((i + 1) % shuffledUsers.size());
            community.getGiftAssignments().put(giver, receiver);
        }

        community.setClose(true);
    }


}
