package it.unicam.cs.Giftify.Model.Repository;

import it.unicam.cs.Giftify.Model.Entity.Community;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommunityRepository extends JpaRepository<Community, Long> {

    List<Community> findByActive(boolean active);

    @NonNull
    Optional<Community> findById(@NonNull Long id);

    Optional<Community> findByAccessCode(String accessCode);
}
