package com.example.firstLOL.user.dto;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
public class JwtRequestDto {
    private String username;
    private String password;
}
