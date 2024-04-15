package com.example.firstLOL.api.service;

import com.example.firstLOL.api.dto.*;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

@Slf4j
@Service
public class ApiService {

    @Value("${riot.api.key}")
    private String apiKey;

    private final RestClient authRestClient;

    public ApiService() {
        this.authRestClient = RestClient
                .builder()
                .build();
    }

    public PuuidDto callRiotApiPuuid(String gameName, String tagLine) {
        String url = UriComponentsBuilder.fromUriString("https://asia.api.riotgames.com/riot/account/v1/accounts/by-riot-id/{gameName}/{tagLine}")
                .queryParam("api_key", this.apiKey)
                .buildAndExpand(gameName, tagLine)
                .toUriString();

        return authRestClient.get()
                .uri(url)
                .retrieve()
                .body(PuuidDto.class);
    }

    public SummonerDto callRiotApiSummonerId(PuuidDto puuidDto) {
        String url = UriComponentsBuilder.fromUriString("https://kr.api.riotgames.com/lol/summoner/v4/summoners/by-puuid/{encryptedPUUID}")
                .queryParam("api_key", this.apiKey)
                .buildAndExpand(puuidDto.getPuuid())
                .toUriString();

        return authRestClient.get()
                .uri(url)
                .retrieve()
                .body(SummonerDto.class);

    }

    public MatchIdDto callRiotApiMatchId(PuuidDto puuidDto) {
        String url = UriComponentsBuilder.fromUriString("https://asia.api.riotgames.com/lol/match/v5/matches/by-puuid/{puuid}/ids")
                .queryParam("queue", 420)
                .queryParam("type", "ranked")
                .queryParam("api_key", this.apiKey)
                .queryParam("count", 10)
                .buildAndExpand(puuidDto.getPuuid())
                .toUriString();

        List<String> matchIdList = authRestClient.get()
                .uri(url)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });

        MatchIdDto matchIdDto = new MatchIdDto();
        matchIdDto.setMatchIdList(matchIdList);

        return matchIdDto;
    }

    public List<Long> callRiotApiMostChampion(String puuid) {
        String url = UriComponentsBuilder.fromUriString("https://kr.api.riotgames.com/lol/champion-mastery/v4/champion-masteries/by-puuid/{encryptedPUUID}/top")
                .queryParam("api_key", apiKey)
                .buildAndExpand(puuid)
                .toUriString();

        List<ChampionMasteryDto> championMasteryDtoList = authRestClient.get()
                .uri(url)
                .retrieve()
                .body(new ParameterizedTypeReference<>(){});

        // 상위 3개 챔피언 ID 추출
        return championMasteryDtoList.stream()
                .limit(3)
                .map(ChampionMasteryDto::getChampionId)
                .collect(Collectors.toList());
    }

    public MatchIdDto callRiotApiMatchIdByTime(String puuid, Long startTime) {

        String url = UriComponentsBuilder.fromUriString("https://asia.api.riotgames.com/lol/match/v5/matches/by-puuid/{puuid}/ids")
                .queryParam("startTime", startTime)
                .queryParam("queue", 420)
                .queryParam("type", "ranked")
                .queryParam("api_key", this.apiKey)
                .queryParam("count", 10)
                .buildAndExpand(puuid)
                .toUriString();

        List<String> matchIdList = authRestClient.get()
                .uri(url)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });

        MatchIdDto matchIdDto = new MatchIdDto();
        matchIdDto.setMatchIdList(matchIdList);

        return matchIdDto;
    }

    public LeagueEntryDTO callRiotApiTier(SummonerDto summonerDto) {
        // summonerId를 받아서 tier로 보내기
        String url = UriComponentsBuilder.fromUriString("https://kr.api.riotgames.com/lol/league/v4/entries/by-summoner/{encryptedSummonerId}")
                .queryParam("api_key", this.apiKey)
                .buildAndExpand(summonerDto.getId())
                .toUriString();

        List leagueEntryDTOS = requireNonNull(authRestClient
                .get()
                .uri(url)
                .retrieve()
                .body(List.class))
                .stream().toList();

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
        // WebClient를 사용하여 비동기적으로 API 호출
        return objectMapper.convertValue(leagueEntryDTOS.get(0), LeagueEntryDTO.class);
    }

    public MatchDto callRiotApiMatch(String matchId) {
        String url = UriComponentsBuilder.fromUriString("https://asia.api.riotgames.com/lol/match/v5/matches/{matchId}")
                .queryParam("api_key", this.apiKey)
                .buildAndExpand(matchId)
                .toUriString();

        MatchDto response = authRestClient.get()
                .uri(url)
                .retrieve()
                .body(MatchDto.class);

        return response;
    }

    public MatchDto.InfoDto.ParticipantDto myInfoFromParticipants(MatchDto matchDto, PuuidDto puuidDto) {
        //플레이어 10명 정보 가져오기

        List<String> participantsPuuid = matchDto.getMetadata().getParticipants();
        //나랑 puuid가 같은 participant의 순서

        String puuid = puuidDto.getPuuid(); //내 puuid
        int index = participantsPuuid.indexOf(puuid);
        //infoDto 안에 participantDto
        List<MatchDto.InfoDto.ParticipantDto> participants = matchDto.getInfo().getParticipants();
        return participants.get(index);
    }
}
