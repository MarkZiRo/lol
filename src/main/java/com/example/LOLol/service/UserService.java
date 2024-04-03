package com.example.LOLol.service;

import com.example.LOLol.controller.SummonerController;
import com.example.LOLol.dto.*;
import com.example.LOLol.entity.UserEntity;
import com.example.LOLol.jwt.JwtTokenUtils;
import com.example.LOLol.repository.UserRepository;
import com.example.LOLol.utils.AuthenticationFacade;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtils jwtTokenUtils;
    private final AuthenticationFacade authenticationFacade;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .map(SummonerUserDetails::fromEntity)
                .orElseThrow(()-> new UsernameNotFoundException("not found"));
    }

    public boolean checkUserDuplication(String username)
    {
        return userRepository.existsByUsername(username);
    }

    @Transactional
    public void createUser(CreateUserDto dto)
    {
        if(userRepository.existsByUsername(dto.getUsername()))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);

     //   service.callRiotAPISummonerName();

        log.info(dto.getPassword());

        UserDto.fromEntity(userRepository.save(UserEntity.builder()
                .username(dto.getUsername())
                .password(passwordEncoder.encode(dto.getPassword()))
                .summonerTag(dto.getSummonerTag())
                .summonerName(dto.getSummonerName())
                .email(dto.getEmail())
                .build()));
    }

    public String issue(JwtRequestDto dto)
    {
        UserEntity userEntity = userRepository.findByUsername(dto.getUsername())
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.UNAUTHORIZED));

        if(!passwordEncoder.matches(dto.getPassword(), userEntity.getPassword()))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);

        String jwt = jwtTokenUtils.generateToken(SummonerUserDetails.fromEntity(userEntity));

        return jwt;
    }

    @Transactional
    public void updateUser(UpdateUserDto dto)
    {
        UserEntity userEntity = authenticationFacade.extractUser();

        userEntity.setEmail(dto.getEmail());
        userEntity.setPassword(dto.getPassword());
        userEntity.setSummonerName(dto.getSummonerName());
        userEntity.setSummonerTag(dto.getSummonerTag());


        UserDto.fromEntity(userRepository.save(userEntity));
    }

    @Transactional
    public void login(JwtRequestDto dto) {
        Optional<UserEntity> optionalUser = userRepository.findByUsername(dto.getUsername());
        if (optionalUser.isEmpty()) {
            return;
        }

        UserEntity user = optionalUser.get();
        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
        }

    }
}
