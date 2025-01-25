package it.unicam.cs.Giftify.Model.Services;

import it.unicam.cs.Giftify.Model.Entity.*;
import it.unicam.cs.Giftify.Model.Repository.AccountCommunityRoleRepository;
import it.unicam.cs.Giftify.Model.Repository.AccountCommunitynameRepository;
import it.unicam.cs.Giftify.Model.Repository.CommunityRepository;
import it.unicam.cs.Giftify.Model.Repository.GiftAssigmentRepository;
import it.unicam.cs.Giftify.Model.Util.AccessCodeGeneretor;
import jakarta.transaction.Transactional;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Servizio per gestire le operazioni relative alle community, come la creazione, eliminazione, gestione degli utenti e altre funzionalità.
 */
@Service
public class CommunityService {

    @Autowired
    private CommunityRepository communityRepository;

    @Autowired
    private AccountCommunityRoleRepository accountCommunityRoleRepository;

    @Autowired
    private AccountService accountServices;

    @Autowired
    private WishListService wishListService;

    @Autowired
    private AccessCodeGeneretor codeGeneretor;

    @Autowired
    private GiftAssigmentRepository giftAssigmentRepository;

    @Autowired
    private AccountCommunitynameRepository accountCommunitynameRepository;

    /**
     * Crea una nuova community, assegnando un amministratore, un budget, una scadenza e una lista dei desideri.
     *
     * @param admin         l'amministratore della community
     * @param name          il nome della community
     * @param note          una descrizione della community
     * @param budget        il budget della community
     * @param deadline      la data di scadenza della community
     * @param communityName il nome dell'utente all'interno della community
     */
    @Transactional
    public void createCommunity(@NonNull Account admin, @NonNull String name, @NonNull String note, double budget, @NonNull LocalDate deadline, @NonNull String communityName) {
        Community community = new Community(codeGeneretor, admin, name, note, budget, deadline);
        WishList wishList = wishListService.createWishList(admin);
        communityRepository.save(community);
        if (community.getAccountByCommunityName(communityName) != null) {
            communityRepository.delete(community);
            throw new IllegalArgumentException("Il nome utente nella comunità è già in uso: " + communityName);
        }
        AccountCommunityName acn = new AccountCommunityName(community.getId(), communityName, admin.getEmail());
        accountCommunitynameRepository.save(acn);
        community.addUser(admin, wishList, acn);
        communityRepository.save(community);
        AccountCommunityRole accountCommunityRole = new AccountCommunityRole(admin.getEmail(), community.getId(), Role.ADMIN);
        accountCommunityRoleRepository.save(accountCommunityRole);
        admin.addOrUpdateRoleForCommunity(accountCommunityRole);
        admin.addCommunity(community);
        accountServices.saveAccount(admin);
    }

    @Transactional
    public void deleteGroup(@NonNull Community community) {

        if (community.getGiftAssignments() != null && !community.getGiftAssignments().isEmpty()) {
            giftAssigmentRepository.deleteAll(community.getGiftAssignments());
        }
        if (community.getUserList() != null && !community.getUserList().isEmpty()) {
            for (Account account : community.getUserList()) {
                Set<AccountCommunityRole> rolesToRemove = account.getCommunityRoles()
                        .stream()
                        .filter(role -> role.getCommunity().equals(community.getId()))
                        .collect(Collectors.toSet());
                rolesToRemove.forEach(role -> {
                    account.removeRoleForCommunity(community);
                    accountCommunityRoleRepository.delete(role);
                });

                account.removeCommunity(community);
                accountServices.saveAccount(account);
            }
        }
        codeGeneretor.removeCode(community.getAccessCode());
        communityRepository.delete(community);
    }

    /**
     * Controlla se le community attive hanno superato la data di scadenza e le segna come inattive.
     */
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

    /**
     * Elimina le community inattive che sono scadute da più di 30 giorni.
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void deleteExpiredGroups() {
        List<Community> inactiveGroups = communityRepository.findByActive(false);
        LocalDate currentDate = LocalDate.now();
        for (Community community : inactiveGroups) {
            if (currentDate.isAfter(community.getDeadline().plusDays(30))) {
                this.deleteGroup(community);
            }
        }
    }


    /**
     * Recupera una community in base al suo ID.
     *
     * @param id l'ID della community
     * @return la community trovata, oppure null se non esiste
     */

    public Community getCommunityById(UUID id) {
        return communityRepository.findById(id).orElse(null);
    }

    /**
     * Recupera tutte le community esistenti.
     *
     * @return una lista di tutte le community
     */
    public List<Community> getAllCommunities() {
        return communityRepository.findAll();
    }

    /**
     * Recupera una community in base al suo codice di accesso.
     *
     * @param accessCode il codice di accesso della community
     * @return la community trovata, oppure null se non esiste
     */
    public Community getCommunityByAccessCode(@NonNull String accessCode) {
        return communityRepository.findByAccessCode(accessCode).orElse(null);
    }

    /**
     * Aggiorna una community esistente.
     *
     * @param community la community da aggiornare
     */
    public void updateCommunity(Community community) {
        communityRepository.save(community);
    }

    /**
     * Aggiunge un utente a una community. L'utente riceverà un ruolo di "MEMBER".
     *
     * @param user      l'utente da aggiungere
     * @param community la community a cui aggiungere l'utente
     * @throws IllegalArgumentException se la community è chiusa o l'utente è già membro
     */
    public void addUserToCommunity(@NonNull Account user, @NonNull Community community, @NonNull String userName) {
        if (community.isClose()) {
            throw new IllegalArgumentException("Impossibile unirsi, il gruppo è chiuso!");
        }
        if (community.getAccountByCommunityName(userName) != null) {

            throw new IllegalArgumentException("Il nome utente nella comunità è già in uso: " + userName);
        }
        if (!community.getUserList().contains(user)) {
            AccountCommunityName acn = new AccountCommunityName(community.getId(), userName, user.getEmail());
            accountCommunitynameRepository.save(acn);
            community.addUser(user, wishListService.createWishList(user), acn);
            communityRepository.save(community);
            user.addCommunity(community);
            AccountCommunityRole accountCommunityRole = new AccountCommunityRole(user.getEmail(), community.getId(), Role.MEMBER);
            accountCommunityRoleRepository.save(accountCommunityRole);
            user.addOrUpdateRoleForCommunity(accountCommunityRole);
            accountServices.saveAccount(user);
        } else throw new IllegalArgumentException("Sei già iscritto a questo gruppo!");
    }

    /**
     * Rimuove un utente da una community.
     *
     * @param user      l'utente da rimuovere
     * @param community la community da cui rimuovere l'utente
     * @throws IllegalArgumentException se la community è chiusa o l'utente non è presente nella community
     */
    public void removeUserFromCommunity(@NonNull Account user, @NonNull Community community) {
        if (community.isClose()) {
            throw new IllegalArgumentException("Impossibile rimuovere un utente se la community è chiusa");
        }
        if (community.getUserList().contains(user)) {
            community.removeUser(user);
            user.removeCommunity(community);
            Set<AccountCommunityRole> roles = user.getCommunityRoles();
            for (AccountCommunityRole role : roles) {
                if (role.getCommunity().equals(community.getId())) {
                    user.removeRoleForCommunity(community);
                    roles.remove(role);
                    accountCommunityRoleRepository.delete(role);
                }
            }
            accountServices.saveAccount(user);
            communityRepository.save(community);
        } else throw new IllegalArgumentException("Impossibile rimuovere, utente non presente.");
    }

    /**
     * Esegue un'estrazione dei nomi tra i partecipanti alla community per assegnare i regali.
     *
     * @param community la community su cui eseguire l'estrazione
     * @throws IllegalArgumentException se il numero di partecipanti non è pari
     */
    public void drawNames(@NonNull Community community) {
        if (community.getUserList().size() % 2 != 0) {
            throw new IllegalArgumentException("Il numero di partecipanti deve essere pari per effettuare l'estrazione.");
        }
        Set<GiftAssignment> accountMap = new HashSet<>();
        List<Account> shuffledUsers = new ArrayList<>(community.getUserList());
        Collections.shuffle(shuffledUsers);
        for (int i = 0; i < shuffledUsers.size(); i++) {
            Account giver = shuffledUsers.get(i);
            Account receiver = shuffledUsers.get((i + 1) % shuffledUsers.size());
            GiftAssignment giftAssignment = new GiftAssignment(giver.getEmail(), receiver.getEmail(), community);
            giftAssigmentRepository.save(giftAssignment);
            accountMap.add(giftAssignment);
        }
        community.setGiftAssignments(accountMap);
        community.setClose(true);
        communityRepository.save(community);
    }



    public String getReceiverName(Account accountGiver, UUID communityId){
        Community community = this.getCommunityById(communityId);
        String receiverEmail = community.getGiftReceiver(accountGiver);
        Account accountReciver = accountServices.getAccount(receiverEmail).get();
        return community.getCommunityNameByAccount(accountReciver);

    }

}
