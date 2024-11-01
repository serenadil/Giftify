package it.unicam.cs.Giftify.Model.Services;

import it.unicam.cs.Giftify.Model.Entity.Community;
import it.unicam.cs.Giftify.Model.Entity.Account;
import it.unicam.cs.Giftify.Model.Repository.CommunityRepository;
import it.unicam.cs.Giftify.Model.Util.AccessCodeGeneretor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class CommunityServices {

    @Autowired
    private CommunityRepository groupRepository;

    public void createCommunity(AccessCodeGeneretor codeGeneretor, Account adminGroup, String groupName,
                            String groupDescription, String groupNote, double budget, LocalDate deadline) {
        Community group = new Community(codeGeneretor, adminGroup, groupName, groupDescription, groupNote, budget, deadline);
        groupRepository.save(group);
    }


    public void deleteGroup(Community Community) {
        groupRepository.delete(Community);
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void checkDeadline() {
        List<Community> activeGroups = groupRepository.findByActive(true);
        for (Community community : activeGroups) {
            if (community.getDeadline().isBefore(LocalDate.now())) {
                community.setActive(false);
                groupRepository.save(community);
            }
        }

    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void deleteGroups() {
        List<Community> inactiveGroups = groupRepository.findByActive(false);
        LocalDate currentDate = LocalDate.now();
        for (Community community : inactiveGroups) {
            if (currentDate.isAfter(community.getDeadline().plusDays(30))) {
                this.deleteGroup(community);
            }
        }
    }


    public Community getCommunityById(long id) {
        return groupRepository.findById(id).orElse(null);
    }


    public Community getCommunityByAccessCode(String accessCode) {
        return groupRepository.findByAccessCode(accessCode).orElse(null);
    }


    public void addUserToCommunity(Account user, Community community) {
        if (!community.getUserList().contains(user)) {
            community.addUser(user);
            groupRepository.save(community);
        } else throw new IllegalArgumentException("Sei già iscritto a questo gruppo!");
    }

    public void removeUserFromCommunity(Account user, Community community) {
        if (community.getUserList().contains(user)) {
            community.removeUser(user);
            groupRepository.save(community);
        } else throw new IllegalArgumentException("Impossibile rimuovere, utente non presente.");

    }


}
