package com.example.firstLOL.user.controller;

import com.example.firstLOL.api.dto.*;
import com.example.firstLOL.api.service.ApiService;
import com.example.firstLOL.user.service.UserService;
import com.example.firstLOL.user.utils.AuthenticationFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Controller
@Slf4j
@RequestMapping("users")
@RequiredArgsConstructor
public class UserSearchController {

    private final ApiService apiService;
    private final UserService userService;
    private final AuthenticationFacade authenticationFacade;

    @PostMapping("/search")
    public String searchSummoner(@RequestParam("gameName") String gameName, @RequestParam("tagLine") String tagLine) {
        // 라이엇 API를 호출하여 소환사 존재 여부 검증
        boolean exists = userService.riotApiCheckGameName(gameName, tagLine);

        if (!exists) {
            // 소환사가 존재하지 않을 경우, fail 페이지로
            return "search-fail";
        }

        // URL 인코딩 수행
        String encodedGameName = URLEncoder.encode(gameName, StandardCharsets.UTF_8);
        String encodedTagLine = URLEncoder.encode(tagLine, StandardCharsets.UTF_8);

        // 인코딩된 값을 사용하여 리디렉션 URL 구성
        return "redirect:/users/" + encodedGameName + "/" + encodedTagLine;
    }

    @GetMapping("/{gameName}/{tagLine}")
    public String userPageForm(
            @PathVariable String gameName,
            @PathVariable String tagLine,
            Model model) throws Exception {

        String decodedGameName = URLDecoder.decode(gameName, StandardCharsets.UTF_8);
        String decodedTagLine = URLDecoder.decode(tagLine, StandardCharsets.UTF_8);

        Authentication authentication = authenticationFacade.getAuth();
        boolean isAuthenticated = authentication != null &&
                !(authentication instanceof AnonymousAuthenticationToken)
                && authentication.isAuthenticated();

        model.addAttribute("isAuthenticated", isAuthenticated);

        PuuidDto puuidDto = apiService.callRiotApiPuuid(gameName, tagLine);
        SummonerDto summonerDto = apiService.callRiotApiSummonerId(puuidDto);
        MatchIdDto matchIdDto = apiService.callRiotApiMatchId(puuidDto);
        LeagueEntryDTO leagueEntryDTO = apiService.callRiotApiTier(summonerDto);

        //최근 10게임 matchIdList
        List<String> matchIdList = matchIdDto.getMatchIdList();
        //최근 10게임 matchDto를 담을 List
        List<MatchDto> matchDtoList = new ArrayList<>();


        List<MatchDto.InfoDto.ParticipantDto> participantDtoList = new ArrayList<>();

        for (String matchId : matchIdList) {
            //한 개의 matchI로 인게임 match 정보 가져오기
            MatchDto matchDto = apiService.callRiotApiMatch(matchId);
            //i번 째 matchDto를 matchDtoList에 담음
            matchDtoList.add(matchDto);
            //i번째 경기 중 내 정보를 담음
            MatchDto.InfoDto.ParticipantDto participantDto = apiService.myInfoFromParticipants(matchDto, puuidDto);
            participantDtoList.add(participantDto);

            System.out.println(matchDto.getInfo().getGameEndTimestamp());
        }

        //model에 넣어서 화면으로 보낸다.
        model.addAttribute("participantDtoList", participantDtoList);
        model.addAttribute("matchDtoList", matchDtoList);
        model.addAttribute("leagueEntryDto", leagueEntryDTO);

        return "user-page";
    }

    @PostMapping("/{gameName}/{tagLine}")
    public String userPage(@PathVariable String gameName,
                           @PathVariable String tagLine) throws IOException {

    //    badgeService.userPageUpdate(gameName, tagLine);

        return "redirect:/users/{gameName}/{tagLine}";
    }
}
