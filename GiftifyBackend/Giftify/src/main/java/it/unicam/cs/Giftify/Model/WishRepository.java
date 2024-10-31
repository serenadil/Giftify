package it.unicam.cs.Giftify.Model;

import org.springframework.data.jpa.repository.JpaRepository;

public interface WishRepository extends JpaRepository<Wish, Long> {
    Wish findByName(String name);
}
