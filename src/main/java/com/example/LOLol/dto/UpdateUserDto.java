package com.example.LOLol.dto;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserDto {

     private String password;
     private String summonerName;
     private String summonerTag;
     private String email;

}
