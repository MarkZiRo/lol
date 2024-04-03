package com.example.LOLol.config;

import com.example.LOLol.jwt.CustomAuthenticationEntryPoint;
import com.example.LOLol.jwt.JwtTokenFilter;
import com.example.LOLol.jwt.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;
import org.springframework.web.client.RestClient;

@Configuration
@RequiredArgsConstructor
//@EnableWebSecurity(debug = true)
public class WebSecurityConfig {

    private final JwtTokenUtils jwtTokenUtils;
    private final UserDetailsService manager;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception
    {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                auth -> auth
                        .requestMatchers( "/users/update")
                        .permitAll()

                        .requestMatchers("https://kr.api.riotgames.com/**"
                                , "/users/create"
                                , "/users/issue"
                                , "/users/my-profile")
                        .permitAll()

                        .anyRequest()
                        .permitAll()
                )
                .formLogin(
                        AbstractHttpConfigurer::disable
                )
//        .formLogin(
//                formLogin -> formLogin
//                        .loginPage("/users/login")
//                        .defaultSuccessUrl("/users/my-profile")
//                        .failureUrl("/users/login?fail")
//                        .permitAll()
//        )

//        .logout(
//                logout -> logout
//                        .logoutUrl("/users/logout")
//                        .logoutSuccessUrl("/users/login")
//        )

        .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        )

        .addFilterBefore(
                new JwtTokenFilter(
                        jwtTokenUtils,
                        manager
                ),
                AuthorizationFilter.class
        )

    ;


        return http.build();
    }

}
