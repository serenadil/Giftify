package it.unicam.cs.Giftify.Model.Services;

import it.unicam.cs.Giftify.Model.Util.AccessCodeGeneretor;
import it.unicam.cs.Giftify.Model.Entity.Group;
import it.unicam.cs.Giftify.Model.Repository.GroupRepository;
import it.unicam.cs.Giftify.Model.Entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class GroupServices {
    @Autowired
    private GroupRepository groupRepository;

    public void createGroup(AccessCodeGeneretor codeGeneretor, User adminGroup, String groupName,
                            String groupDescription, String groupNote, double budget, LocalDate deadline) {
        Group group = new Group(codeGeneretor, adminGroup, groupName, groupDescription, groupNote, budget, deadline);
        groupRepository.save(group);
    }


    public void deleteGroupByAdmin(User adminGroup, Group group) {
        if (adminGroup.equals(group.getAdminGroup())) {
            groupRepository.delete(group);
        } else {
            throw new IllegalArgumentException("impossibile autorizzare l'operazione");
        }
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void checkDeadline() {
        List<Group> activeGroups = groupRepository.findByActive(true);
        for (Group group : activeGroups) {
            if (group.getDeadline().isBefore(LocalDate.now())) {
                group.setActive(false);
                groupRepository.save(group);
            }
        }

    }


}
