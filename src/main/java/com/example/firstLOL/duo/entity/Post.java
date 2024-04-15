package com.example.firstLOL.duo.entity;

import com.example.firstLOL.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    private String memo;

    @Setter
    private Long userId;

    @Setter
    private String myPosition;
    @Setter
    private String findPosition;
    @Setter
    private String status;
    @Setter
    @CreationTimestamp
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE)
    private List<Offer> offer;

    @Setter
    // userEntity 필드 추가
    @ManyToOne(fetch = FetchType.LAZY)
    private UserEntity userEntity;


}
