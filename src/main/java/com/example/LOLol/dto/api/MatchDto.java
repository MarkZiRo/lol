package com.example.LOLol.dto.api;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;

@Getter
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class MatchDto {

    private MetadataDto metadata;
    private InfoDto info;

    public static class MetadataDto {

        public String dataVersion;
        public String matchId;
        public List<String> participants;
    }

    public static class InfoDto{

        public long gameEndTimestamp;
        public long gameStartTimestamp;
        public String gameType;
        public List<ParticipantDto> participants;

        public static class ParticipantDto
        {
            public Integer assists;
            public Integer baronKills;

            public Integer doubleKills;
            public Integer dragonKills;

            public Integer goldEarned;
            public Integer goldSpent;

            public String individualPosition;

            public Integer item0;
            public Integer item1;
            public Integer item2;
            public Integer item3;
            public Integer item4;
            public Integer item5;
            public Integer item6;

            public Integer kills;
            public Integer deaths;
            public Integer kda;

            public Integer neutralMinionsKilled;
            public Integer pentaKills;

            public Integer profileIcon;
            public Integer quadraKills;

            public String riotIdName;
            public String riotIdTagline;

            public Integer totalDamageDealtToChampions;
            public Integer totalDamageTaken;

            public Integer tripleKills;

            public Integer visionScore;
            public Integer visionWardsBoughtInGame;
            public Integer wardsKilled;
            public Integer wardsPlaced;

            public boolean win;

        }
    }
}
