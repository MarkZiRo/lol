package com.example.LOLol.dto;

import com.example.LOLol.dto.api.ChampionMasteryDto;
import com.example.LOLol.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class UserDto {

    private Long id;
    private String username;
    private String summonerTag;
    private String summonerName;
    private String email;
    private String puuid;
    private List<Integer> myMostChampion;
    private List<String> myGameList;

    private List<String> roles;


    public static UserDto fromEntity(UserEntity entity)
    {
       List<String> roles = Arrays.stream(entity.getRoles().split(",")).toList();

        return UserDto.builder()
                .id(entity.getId())
                .username(entity.getUsername())
                .summonerTag(entity.getSummonerTag())
                .summonerName(entity.getSummonerName())
                .email(entity.getEmail())
                .puuid(entity.getPuuid())
                .myMostChampion(entity.getMostChampionIds())
                .myGameList(entity.getMatchIds())
                .roles(roles)
                .build();
    }
}
