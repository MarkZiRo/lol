package com.example.firstLOL.duo.dto;

import com.example.firstLOL.duo.entity.Offer;
import com.example.firstLOL.duo.entity.Post;
import com.example.firstLOL.user.entity.UserEntity;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OfferDto {

    private Long id;
    @Setter
    private String status;
    @Setter
    private Long applyUserId;
    @Setter
    private LocalDateTime createdAt;
    @Setter
    private Post post;
    @Setter
    private UserEntity userEntity;

    public static OfferDto fromEntity(Offer entity){
        OfferDto.OfferDtoBuilder builder = OfferDto.builder()
                .id(entity.getId())
                .status(entity.getStatus())
                .applyUserId(entity.getApplyUserId())
                .createdAt(entity.getCreatedAt())
                .post(entity.getPost())
                .userEntity(entity.getUserEntity());

        return builder.build();
    }
}

