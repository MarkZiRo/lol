package com.example.firstLOL.duo.service;

import com.example.firstLOL.duo.dto.OfferDto;
import com.example.firstLOL.duo.entity.Offer;
import com.example.firstLOL.duo.entity.Post;
import com.example.firstLOL.duo.repository.OfferRepository;
import com.example.firstLOL.duo.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class OfferService {

    private final OfferRepository offerRepository;
    private final PostRepository postRepository;

    public OfferDto createDuo(Long postId, OfferDto offerDto){
        Offer offer = new Offer();
        Post post = postRepository.findById(postId).orElseThrow();

        offerDto.setStatus("신청함");
        offerDto.setApplyUserId(offerDto.getApplyUserId());
        offerDto.setPost(post);
        offer.setUserEntity(offerDto.getUserEntity());
        offer.setStatus(offerDto.getStatus());
        offer.setApplyUserId(offerDto.getApplyUserId());
        offer.setPost(offerDto.getPost());
        offer.setApplyUserId(offerDto.getApplyUserId());

        return OfferDto.fromEntity(offerRepository.save(offer));
    }

    public void deleteOffer(Long postId, Long userId){
        Post post = postRepository.findById(postId).orElseThrow();
        offerRepository.deleteOfferByPostAndApplyUserId(post, userId);
    }

    public void deleteOffer(Long offerId){
        Offer offer = offerRepository.findById(offerId).orElseThrow();
        offerRepository.delete(offer);
    }

    public OfferDto updateStatus(Long offerId, String status){
        Offer offer = offerRepository.findById(offerId).orElseThrow();
        offer.setStatus(status);

        return OfferDto.fromEntity(offerRepository.save(offer));
    }

    public Offer readOfferOne(Long offerId){
        return offerRepository.findById(offerId).orElseThrow();
    }

    public void deleteAnotherOffer(Long offerId){
        offerRepository.deleteByIdNot(offerId);
    }

}
