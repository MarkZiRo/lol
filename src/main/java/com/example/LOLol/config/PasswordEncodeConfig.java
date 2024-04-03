package com.example.LOLol.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class PasswordEncodeConfig {

//    BCrypt는 비밀번호를 안전하게 저장하기 위한 해시 함수입니다. BCrypt는 비밀번호 해싱을 위해 Blowfish 암호화 알고리즘을 사용하며,
//    암호화된 비밀번호를 저장할 때 임의의 솔트(salt)를 생성하여 비밀번호의 보안성을 높입니다.


    @Bean
    public PasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder();
    }
}
