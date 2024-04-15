package com.example.firstLOL.user.utils;


import com.example.firstLOL.user.dto.CustomUserDetails;
import com.example.firstLOL.user.entity.UserEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationFacade {
    //사용자 인증 여부 확인하여 인증 객체 반환
    public Authentication getAuth() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    //사용자 정보 추출하여 사용자 엔티티 반환
    public UserEntity extractUser() {
        CustomUserDetails userDetails = (CustomUserDetails) getAuth().getPrincipal();
        return userDetails.getEntity();
    }

}
