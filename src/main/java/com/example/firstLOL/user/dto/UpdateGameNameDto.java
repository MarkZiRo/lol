package com.example.firstLOL.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateGameNameDto {
    @NotBlank(message = "소환사 닉네임은 필수입니다")
    private String gameName;
    @NotBlank(message = "게임 태그는 필수입니다.")
    private String tagLine;
}
