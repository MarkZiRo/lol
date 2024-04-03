package com.example.LOLol.service;

import com.example.LOLol.dto.*;
import com.example.LOLol.dto.api.ChampionMasteryDto;
import com.example.LOLol.dto.api.LeagueEntryDTO;
import com.example.LOLol.dto.api.MatchDto;
import com.example.LOLol.dto.api.SummonerDto;
import com.example.LOLol.entity.UserEntity;
import com.example.LOLol.repository.UserRepository;
import com.example.LOLol.utils.AuthenticationFacade;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.text.SimpleDateFormat;
import java.util.*;


@Slf4j
@Service
public class SummonerService {

    @Value("${riot.api.key}")
    private String apiKey;

    private String puuid = "B9G1pVBnnHCGrBmSO5DLsnUNmCe4LsAIqzx_ErRXwejfha_Wi_erQK7bBEsuTvYG58H8sE6LnDBf9g";

    private final RestClient authRestClient;
    private final UserRepository userRepository;
    private final AuthenticationFacade authenticationFacade;

    public SummonerService(UserRepository userRepository, UserService service, AuthenticationFacade authenticationFacade)
    {
        this.authenticationFacade = authenticationFacade;
        this.userRepository = userRepository;
        this.authRestClient =RestClient
                .builder()
                .build();

    }

    @Transactional
    public void callRiotAllAPI()
    {
        callRiotAPISummonerName();
        callRiotAPIMostChampion();
        callRiotAPIMatchId();
        callRiotAPIMatch();
        callRiotApiTier();
    }

    public void callRiotAPISummonerName()
    {
        UserEntity userEntity = authenticationFacade.extractUser();

        String url = UriComponentsBuilder.fromUriString("https://kr.api.riotgames.com/lol/summoner/v4/summoners/by-name/")
                .path(userEntity.getSummonerName())
                .queryParam("api_key", this.apiKey)
                .build(false)
                .toUriString();

        log.info(url);

        SummonerDto response = authRestClient.get()
                .uri(url)
                .retrieve()
                .body(SummonerDto.class);


        userEntity.setPuuid(response.getPuuid());
        userEntity.setLeagueId(response.getId());


        UserDto.fromEntity(userRepository.save(userEntity));

    }

    @Transactional
    public void callRiotAPIMostChampion()
    {

        UserEntity userEntity = authenticationFacade.extractUser();

        String url = UriComponentsBuilder.fromUriString("https://kr.api.riotgames.com/lol/champion-mastery/v4/champion-masteries/by-puuid/")
                .path(userEntity.getPuuid())
                .path("/top")
                .queryParam("api_key", this.apiKey)
                .build(false)
                .toUriString();

        List<ChampionMasteryDto> response = authRestClient.get()
                .uri(url)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });

        log.info(response.toString());

        for (ChampionMasteryDto championMasteryDto : response) {
            userEntity.mostChampionIds.add(championMasteryDto.getChampionId());
        }

        UserDto.fromEntity(userRepository.save(userEntity));
    }

    @Transactional
    public void callRiotAPIMatchId()
    {

        UserEntity userEntity = authenticationFacade.extractUser();

        String url = UriComponentsBuilder.fromUriString("https://asia.api.riotgames.com/lol/match/v5/matches/by-puuid/")
                .path(userEntity.getPuuid())
                .path("/ids?start=0&count=20&api_key=")
                .path(this.apiKey)
                .build(false)
                .toUriString();

        log.info(url);

        List<String> response = authRestClient.get()
                .uri(url)
                .retrieve()
                .body(List.class);

        assert response != null;

        userEntity.matchIds.addAll(response);

        UserDto.fromEntity(userRepository.save(userEntity));
    }


    @Transactional
    public MatchDto callRiotAPIMatch()
    {
        UserEntity userEntity = authenticationFacade.extractUser();

        String url = UriComponentsBuilder.fromUriString("https://asia.api.riotgames.com/lol/match/v5/matches/")
                .path(userEntity.matchIds.get(0))
                .queryParam("api_key", this.apiKey)
                .build(false)
                .toUriString();

        log.info(url);

        MatchDto response = authRestClient.get()
                .uri(url)
                .retrieve()
                .body(MatchDto.class);

        log.info(response.toString());

        SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
        Date date1 = new Date();
        Date date2 = new Date();
        date1.setTime(response.getInfo().gameStartTimestamp);
        date2.setTime(response.getInfo().gameEndTimestamp);

        String dateStartTime = sdf.format(date1);
        String dateEndTime = sdf.format(date2);

        log.info(dateStartTime);
        log.info(dateEndTime);

       return response;
        //   return UserDto.fromEntity(userRepository.save(userEntity));
    }

    public void callRiotApiTier()  {

        UserEntity userEntity = authenticationFacade.extractUser();

        String url = UriComponentsBuilder.fromUriString("https://kr.api.riotgames.com/lol/league/v4/entries/by-summoner/{encryptedSummonerId}")
                .queryParam("api_key", this.apiKey)
                .buildAndExpand(userEntity.getLeagueId())
                .toUriString();

        List<LeagueEntryDTO> leagueEntryDTOS = Objects.requireNonNull(authRestClient
                        .get()
                        .uri(url)
                        .retrieve()
                        .body(List.class))
                .stream().toList();

//        log.info(leagueEntryDTO.toString());
//        log.info(leagueEntryDTO.getTier());


        ObjectMapper objectMapper = new ObjectMapper();
       objectMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
       LeagueEntryDTO leagueEntryDTO = objectMapper.convertValue(leagueEntryDTOS.get(0), LeagueEntryDTO.class);


        userEntity.setTier(leagueEntryDTO.getTier());
        userEntity.setRank(leagueEntryDTO.getRank());
        userEntity.setLeaguePoints(leagueEntryDTO.getLeaguePoints());
        userEntity.setWins(leagueEntryDTO.getWins());
        userEntity.setLosses(leagueEntryDTO.getLosses());

        UserDto.fromEntity(userRepository.save(userEntity));

    }

}
