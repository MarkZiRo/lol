package com.example.firstLOL.user.repository;


import com.example.firstLOL.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity,Long> {

    Optional<UserEntity> findByUsername(String username);
    Optional<UserEntity> findByGameNameAndTagLine(String gameName, String tagLine);
    boolean existsByUsername(String username);
    boolean existsByGameNameAndTagLine(String gameName, String tagLine);

}
