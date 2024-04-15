package com.example.firstLOL.duo.service;

import com.example.firstLOL.duo.dto.PostDto;
import com.example.firstLOL.duo.entity.Post;
import com.example.firstLOL.duo.repository.OfferRepository;
import com.example.firstLOL.duo.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final OfferRepository offerRepository;

    public List<PostDto> readAll()
    {
        List<PostDto> postDtos = new ArrayList<>();

        for (Post post : postRepository.findAllByOrderByCreatedAtDesc()) {
            postDtos.add(PostDto.fromEntity(post));
        }

        return postDtos;
    }

    public PostDto createDuo(PostDto postDto) {
        Post post = new Post();

        if (postRepository.existsByStatusAndUserId("구인중", postDto.getUserId())){
            System.out.println("이미 구인중");
            return null;
        }
         else {
            post.setMemo(postDto.getMemo());
            post.setMyPosition(postDto.getMyPosition());
            post.setFindPosition(postDto.getFindPosition());
            post.setStatus("구인중");
            post.setUserId(postDto.getUserId());
            post.setUserEntity(postDto.getUserEntity());
            return PostDto.fromEntity(postRepository.save(post));
        }
    }

    public PostDto readPost(Long postId){
        return PostDto.fromEntity(postRepository.findById(postId).orElseThrow());
    }

    public void deletePost(Long postId){
        Post post = postRepository.findById(postId).orElseThrow();

        postRepository.delete(post);
    }

    public PostDto updateStatus(Long postId, String status){
        Post post = postRepository.findById(postId).orElseThrow();
        post.setStatus(status);

        return PostDto.fromEntity(postRepository.save(post));
    }

    public void updateTime(Long postId){
        Post post = postRepository.findById(postId).orElseThrow();
        post.setCreatedAt(LocalDateTime.now());

        postRepository.save(post);
    }
}
