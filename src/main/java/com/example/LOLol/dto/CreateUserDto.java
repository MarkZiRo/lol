package com.example.LOLol.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserDto {

    @NotBlank(message = "아이디를 입력해주세요.")
    private String username;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String password;

    @NotBlank(message = "tag를 입력해주세요.")
    private String summonerTag;

    @NotBlank(message = "소환사이름을 입력해주세요.")
    private String summonerName;

    @NotBlank(message = "이메일을 입력해주세요.")
    private String email;
}
