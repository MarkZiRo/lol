package com.example.firstLOL.duo.repository;

import com.example.firstLOL.duo.entity.Offer;
import com.example.firstLOL.duo.entity.Post;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OfferRepository extends JpaRepository<Offer, Long> {

    List<Offer> findByPost(Post post);

    void deleteOfferByPostAndApplyUserId(Post post, Long userId);

    @Transactional
    void deleteByIdNot(Long offerId);
}
