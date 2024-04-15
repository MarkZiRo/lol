package com.example.firstLOL.duo.dto;


import com.example.firstLOL.duo.entity.Offer;
import com.example.firstLOL.duo.entity.Post;
import com.example.firstLOL.user.entity.UserEntity;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostDto {
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
    private LocalDateTime createdAt;
    @Setter
    private List<Offer> offer;
    @Setter
    private UserEntity userEntity;

    //TODO 권한 완료하면 권한에 부여 해야 함.
    public static PostDto fromEntity(Post entity){
        PostDtoBuilder builder = PostDto.builder()
                .id(entity.getId())
                .memo(entity.getMemo())
                .userId(entity.getUserId())
                .myPosition(entity.getMyPosition())
                .findPosition(entity.getFindPosition())
                .status(entity.getStatus())
                .createdAt(entity.getCreatedAt())
                .offer(entity.getOffer())
                .userEntity(entity.getUserEntity());

        return builder.build();
    }

}
