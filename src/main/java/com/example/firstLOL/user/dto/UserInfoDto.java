package com.example.firstLOL.user.dto;


import com.example.firstLOL.user.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoDto {

    private Long id;
    private String username;
    private String gameName;
    private String tagLine;
    private String email;
    private String phone;
    private String tier;

    private List<String> roles;

    public static UserInfoDto fromEntity(UserEntity entity)
    {
       List<String> roles = Arrays.stream(entity.getRoles().split(",")).toList();
        return UserInfoDto.builder()
                .id(entity.getId())
                .username(entity.getUsername())
                .gameName(entity.getGameName())
                .tagLine(entity.getTagLine())
                .email(entity.getEmail())
                .phone(entity.getPhone())
                .roles(roles)
                .build();
    }
}
