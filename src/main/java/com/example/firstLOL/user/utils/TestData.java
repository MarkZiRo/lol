package com.example.firstLOL.user.utils;

import com.example.firstLOL.user.entity.UserEntity;
import com.example.firstLOL.user.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TestData {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public TestData(UserRepository userRepository, PasswordEncoder passwordEncoder)
    {
        this.userRepository =userRepository;
        this.passwordEncoder = passwordEncoder;
        addUsers();
    }

    private void addUsers() {
        userRepository.saveAll(List.of(UserEntity.builder()
                .username("as")
                .password(passwordEncoder.encode("12"))
                .tagLine("KR1")
                .gameName("hide on bush")
                .email("yhj216@han")
                .phone("010")
                .build(),
                UserEntity.builder()
                        .username("asd")
                        .password(passwordEncoder.encode("12"))
                        .tagLine("9003")
                        .gameName("ziromark")
                        .email("yhj216@han")
                        .phone("010")
                        .build(),
                UserEntity.builder()
                        .username("asdf")
                        .password(passwordEncoder.encode("12"))
                        .tagLine("KR3")
                        .gameName("괴물쥐")
                        .email("yhj216@han")
                        .phone("010")
                        .build()
        ));
    }

}
