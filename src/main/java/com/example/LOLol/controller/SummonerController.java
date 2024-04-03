package com.example.LOLol.controller;

import com.example.LOLol.dto.UserDto;
import com.example.LOLol.dto.api.LeagueEntryDTO;
import com.example.LOLol.dto.api.MatchDto;
import com.example.LOLol.service.SummonerService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@Slf4j
@RestController
@RequiredArgsConstructor
public class SummonerController {

    private final SummonerService service;

    @GetMapping("summonerName")
    public void callSummonerName()
    {
       service.callRiotAPISummonerName();
    }

    @GetMapping("mostChampion")
    public void champion()
    {
       service.callRiotAPIMostChampion();
    }

    @GetMapping("matchId")
    public void matchId() {
        service.callRiotAPIMatchId();
    }

    @GetMapping("match")
    public MatchDto match()
    {
         return service.callRiotAPIMatch();
    }

    @GetMapping("tier")
    public void tier() {
         service.callRiotApiTier();
    }

}
