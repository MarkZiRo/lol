package com.example.firstLOL.duo.repository;


import com.example.firstLOL.duo.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    boolean existsByStatusAndUserId(String status, Long userId);

    List<Post> findAllByOrderByCreatedAtDesc();
}
