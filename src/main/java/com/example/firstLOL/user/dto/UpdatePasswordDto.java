package com.example.firstLOL.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdatePasswordDto {
    @NotBlank(message = "현재 비밀번호는 필수입니다.")
    private String currentPassword;

    @NotBlank(message = "새 비밀번호는 필수입니다.")
    private String newPassword;

    @NotBlank(message = "새 비밀번호 확인은 필수입니다.")
    private String confirmPassword;
}
