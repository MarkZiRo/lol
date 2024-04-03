package com.example.LOLol.dto.api;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LeagueEntryDTO {

    private String leagueId;
    private String summonerId;
    private String summonerName;
    private String queueType;

    private boolean hotStreak;
    private boolean veteran;
    private boolean freshBlood;
    private boolean inactive;
    private MiniSeriesDTO miniSeries;

    private String tier;
    private String rank;

    private Integer leaguePoints;

    private Integer wins;
    private Integer losses;


    public static class MiniSeriesDTO
    {
        public Integer losses;
        public String progress;
        public Integer target;
        public Integer wins;
    }


}
