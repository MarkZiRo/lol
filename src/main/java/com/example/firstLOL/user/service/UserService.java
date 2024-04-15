package com.example.firstLOL.user.service;

import com.example.firstLOL.api.dto.LeagueEntryDTO;
import com.example.firstLOL.api.dto.PuuidDto;
import com.example.firstLOL.api.dto.SummonerDto;
import com.example.firstLOL.api.service.ApiService;
import com.example.firstLOL.user.dto.*;
import com.example.firstLOL.user.dto.CustomUserDetails;
import com.example.firstLOL.user.entity.UserEntity;
import com.example.firstLOL.user.repository.UserRepository;
import com.example.firstLOL.user.utils.AuthenticationFacade;
import com.example.firstLOL.user.utils.ChampionDataLoader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final ApiService apiService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationFacade authenticationFacade;
    private final ChampionDataLoader championDataLoader;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .map(CustomUserDetails::fromEntity)
                .orElseThrow(() -> new UsernameNotFoundException("not found"));
    }

    public boolean checkUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public boolean checkGameName(String gameName, String tagLine){
        return userRepository.existsByGameNameAndTagLine(gameName, tagLine);
    }

    @Transactional
    public UserEntity getUserByUsername(String username) {
        if (username == null) return null;
        Optional<UserEntity> optionalUser = userRepository.findByUsername(username);
        return optionalUser.orElse(null);
    }

    public boolean riotApiCheckGameName(String gameName, String tagLine) {
        try {
            //입력 받은 tagLine과 gameName으로 puuid 가져오기
            PuuidDto puuidDto = apiService.callRiotApiPuuid(gameName, tagLine);
            // puuidDto에 puuid가 잘 들어있으면 존재하는 것으로 간주
            return puuidDto != null && !puuidDto.getPuuid().isEmpty();

        } catch (Exception e) {
            // API 호출 실패 (예: 네트워크 문제, 잘못된 입력 등)
            return false;
        }
    }

    //회원가입
    @Transactional(timeout = 10)
    public void createUser(CreateUserDto dto) throws IOException {

        //프로필 아이콘 주소 : https://ddragon.leagueoflegends.com/cdn/10.6.1/img/profileicon/{profileIconId}.png
        PuuidDto puuidDto = apiService.callRiotApiPuuid(dto.getGameName(), dto.getTagLine());
        SummonerDto summonerDto = apiService.callRiotApiSummonerId(puuidDto);
        LeagueEntryDTO leagueEntryDTO = apiService.callRiotApiTier(summonerDto);

        List<String> mostChampionNames = getMostChampionNames(puuidDto.getPuuid());

        userRepository.save(dto.toEntity(passwordEncoder.encode(dto.getPassword()), leagueEntryDTO.getTier(),
                puuidDto.getPuuid(), summonerDto.getProfileIconId(), LocalDateTime.now(),
                mostChampionNames.get(0),mostChampionNames.get(1), mostChampionNames.get(2)));
    }

    public List<String> getMostChampionNames(String puuid) throws IOException {
        List<Long> championIds = apiService.callRiotApiMostChampion(puuid);
        return championIds.stream()
                .map(championId -> championDataLoader.findChampionIdByKey(String.valueOf(championId)))
                .collect(Collectors.toList());
    }

    @Transactional
    public UserEntity login(JwtRequestDto dto) {
        Optional<UserEntity> optionalUser = userRepository.findByUsername(dto.getUsername());
        if (optionalUser.isEmpty()) {
            return null;
        }
        //비밀번호 확인
        UserEntity user = optionalUser.get();
        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            return null;
        }

        return user;
    }

    public boolean checkCurrentPassword(String password) {
        // 현재 인증된 사용자의 비밀번호 가져오기
        UserEntity user = authenticationFacade.extractUser();
        return passwordEncoder.matches(password, user.getPassword());
    }

    @GetMapping("/authorization-fail")
    public String authorizationFail(){
        return "authorization-fail";
    }

    @Transactional
    public void updateUser(UpdateUserDto dto) {
        UserEntity user = authenticationFacade.extractUser();

        user.setPhone(dto.getPhone());
        user.setEmail(dto.getEmail());

        UserInfoDto.fromEntity(userRepository.save(user));
    }

    @Transactional
    public void updatePassword(UpdatePasswordDto dto) {
        UserEntity user = authenticationFacade.extractUser();

        // 새 비밀번호 암호화
        String encodedNewPassword = passwordEncoder.encode(dto.getNewPassword());
        user.setPassword(encodedNewPassword);

        // 변경 사항 저장
        userRepository.save(user);
    }

    @Transactional
    public void updateGameName(UpdateGameNameDto dto){
        UserEntity user = authenticationFacade.extractUser();

        PuuidDto puuidDto = apiService.callRiotApiPuuid(dto.getGameName(), dto.getTagLine());
        SummonerDto summonerDto = apiService.callRiotApiSummonerId(puuidDto);
        LeagueEntryDTO leagueEntryDTO = apiService.callRiotApiTier(summonerDto);

        user.setGameName(dto.getGameName());
        user.setTagLine(dto.getTagLine());
        user.setPuuid(puuidDto.getPuuid());
        user.setTier(leagueEntryDTO.getTier());
        user.setProfileIconId(summonerDto.getProfileIconId());
        userRepository.save(user);
    }

    public void updateTrust(Long userId, Integer trustScore){

        UserEntity user = userRepository.findById(userId).orElseThrow();
        int score = 0;
        if (user.getTrustScore() != null) {
            score = user.getTrustScore();
        }
        score = score + trustScore;
        user.setTrustScore(score);

        userRepository.save(user);
    }

    public UserEntity findById(Long userId){
        return userRepository.findById(userId).orElseThrow();
    }
}
