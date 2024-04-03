package com.example.LOLol.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String password;

    private String summonerName;

    private String summonerTag;

    private String puuid;

    private String email;

    private String leagueId;

   @ElementCollection(fetch = FetchType.EAGER)
   public List<Integer> mostChampionIds = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    public List<String> matchIds = new ArrayList<>();

    @Builder.Default
    private String roles = "ROLE_GUEST";

    private String tier;
    private String rank;
    private Integer leaguePoints;

    private Integer wins;
    private Integer losses;

}
