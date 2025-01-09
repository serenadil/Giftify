package it.unicam.cs.Giftify.Model.Repository;

import it.unicam.cs.Giftify.Model.Entity.Community;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CommunityRepository extends JpaRepository<Community, UUID> {

    List<Community> findByActive(boolean active);

    @NonNull
    Optional<Community> findById(@NonNull UUID id);

    Optional<Community> findByAccessCode(String accessCode);
}
