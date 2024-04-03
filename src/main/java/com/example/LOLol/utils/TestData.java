package com.example.LOLol.utils;

import com.example.LOLol.entity.UserEntity;
import com.example.LOLol.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
        userRepository.save(UserEntity.builder()
                .username("12")
                .password(passwordEncoder.encode("as"))
                .build());
    }

}
