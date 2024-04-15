package com.example.firstLOL.user.entity;

import com.example.firstLOL.duo.entity.Offer;
import com.example.firstLOL.duo.entity.Post;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
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

    private String puuid;

    private String password;

    private String gameName;

    private String tagLine;

    private String email;

    private String phone;

    private String tier;

    private Integer dailyGameCount;

    private Integer profileIconId;

    private Integer leagueWins;

    private Integer leagueLosses;

    @CreationTimestamp
    private LocalDateTime createdAt;


    private LocalDateTime updatedAt;        // 유저 페이지에서 최근 업데이트 된 시간

//    @Setter
//    @OneToMany(mappedBy = "following", cascade = CascadeType.ALL)
//    private List<Follow> followerList;
//
//    @Setter
//    @OneToMany(mappedBy = "follower", cascade = CascadeType.ALL)
//    private List<Follow> followingList;
//
    @Builder.Default
    private String roles = "ROLE_USER";
//
//    @Setter
//    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
//    private Quest quest;
//
//    @Setter
//    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
//    private Set<UserBadge> userBadges;
//

    private Integer trustScore;

    private Integer level;

    private String firstChampion;

    private String secondChampion;

    private String thirdChampion;

    @Setter
    @OneToMany(mappedBy = "userEntity")
    private List<Post> post;

    @Setter
    @OneToMany(mappedBy = "userEntity")
    private List<Offer> offer;

//    //유저 생성할 때 Quest도 생성
//    @PrePersist
//    public void initializeQuest() {
//        if (this.quest == null) {
//            Quest newQuest = new Quest();
//            // Quest 기본값 설정
//            newQuest.setUser(this); // 양방향 설정
//            this.quest = newQuest;
//        }
//    }

}
