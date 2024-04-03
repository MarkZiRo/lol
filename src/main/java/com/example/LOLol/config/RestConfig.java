package com.example.LOLol.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
@RequiredArgsConstructor
public class RestConfig {

   // private final tokenService;

   // @Bean
    public RestClient lolClient()
    {
        return RestClient.builder()
                .baseUrl("https://kr.api.riotgames.com")
                .build();
    }

}
