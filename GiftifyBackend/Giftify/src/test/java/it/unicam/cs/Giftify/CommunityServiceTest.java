package it.unicam.cs.Giftify;

import it.unicam.cs.Giftify.Model.Entity.Account;
import it.unicam.cs.Giftify.Model.Entity.Community;
import it.unicam.cs.Giftify.Model.Repository.CommunityRepository;
import it.unicam.cs.Giftify.Model.Services.AccountService;
import it.unicam.cs.Giftify.Model.Services.CommunityService;
import it.unicam.cs.Giftify.Model.Services.WishListService;
import it.unicam.cs.Giftify.Model.Util.AccessCodeGeneretor;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class CommunityServiceTest {


    @Autowired
    private CommunityService communityService;

    @Autowired
    private CommunityRepository communityRepository;

    @Autowired
    private AccountService accountService;

    @Autowired
    private WishListService wishListService;

    @Test
    void testCreateCommunity() {
        AccessCodeGeneretor codeGenerator = new AccessCodeGeneretor();
        Account admin = new Account("user22@example.com", "jdyubv652656");
        accountService.saveAccount(admin);
        String name = "Test Community";
        String note = "No special notes";
        double budget = 100.0;
        LocalDate deadline = LocalDate.now().plusDays(10);
        communityService.createCommunity(admin, name, note, budget, deadline);
        List<Community> communities = communityRepository.findAll();
        assertEquals(1, communities.size());
        Community createdCommunity = communities.get(0);
        assertEquals(name, createdCommunity.getCommunityName());
        assertEquals(note, createdCommunity.getCommunityNote());
        assertEquals(admin, createdCommunity.getAdmin());
    }

    @Test
    void testAddUserToCommunity() {
        AccessCodeGeneretor codeGenerator = new AccessCodeGeneretor();
        Account admin = new Account("us88er@example.com", "jdyubv652656");
        accountService.saveAccount(admin);
        Community community = new Community(codeGenerator, admin, "Test Community", "Note", 100.0, LocalDate.now().plusDays(10));
        communityRepository.save(community);
        Account user = new Account("user11@example.com", "jdyubv652656");
        accountService.saveAccount(user);
        communityService.addUserToCommunity(user, community);
        Community updatedCommunity = communityRepository.findById(community.getId()).orElseThrow();
        assertTrue(updatedCommunity.getUserList().contains(user));
    }

    @Test
    void testRemoveUserFromCommunity() {
        AccessCodeGeneretor codeGenerator = new AccessCodeGeneretor();
        Account admin = new Account("user4144@example.com", "jdyubv652656");
        accountService.saveAccount(admin);
        Community community = new Community(codeGenerator, admin, "Test Community", "Note", 100.0, LocalDate.now().plusDays(10));
        communityRepository.save(community);
        Account user = new Account( "user@example.com", "jdyubv652656");
        community.addUser(user, wishListService.createWishList(user));
        accountService.saveAccount(user);
        communityRepository.save(community);
        communityService.removeUserFromCommunity(user, community);

        Community updatedCommunity = communityRepository.findById(community.getId()).orElseThrow();
        assertFalse(updatedCommunity.getUserList().contains(user));
    }

    @Test
    void testDrawNames() {
        AccessCodeGeneretor codeGenerator = new AccessCodeGeneretor();
        Account admin = new Account( "admin@example.com", "Pluto2235");
        accountService.saveAccount(admin);
        Community community = new Community(codeGenerator, admin, "Test Community", "Note", 100.0, LocalDate.now().plusDays(10));
        Account user1 = new Account("user1@example.com", "PippoL55");
        Account user2 = new Account("user2@example.com", "lcdd22222");
        community.addUser(user1, wishListService.createWishList(user1));
        community.addUser(user2, wishListService.createWishList(user2));
        accountService.saveAccount(user1);
        accountService.saveAccount(user2);
        communityRepository.save(community);
        communityService.drawNames(community);
        Community updatedCommunity = communityRepository.findById(community.getId()).orElseThrow();
        assertTrue(updatedCommunity.isClose());
        assertNotNull(updatedCommunity.getGiftAssignments());
        assertEquals(2, updatedCommunity.getGiftAssignments().size());
    }

    @Test
    void testCheckDeadline_ThrowsExceptionForPastDeadline() {
        AccessCodeGeneretor codeGenerator = new AccessCodeGeneretor();
        Account admin = new Account("user44@example.com", "jdyubv652656");
        accountService.saveAccount(admin);
        String communityName = "Active Community";
        String communityNote = "Note";
        double budget = 100.0;
        LocalDate pastDeadline = LocalDate.now().minusDays(1);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new Community(codeGenerator, admin, communityName, communityNote, budget, pastDeadline);
        });
        assertEquals("deadline non valida", exception.getMessage());
    }

}
