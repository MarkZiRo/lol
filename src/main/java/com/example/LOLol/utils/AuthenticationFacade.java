package com.example.LOLol.utils;

import com.example.LOLol.dto.SummonerUserDetails;
import com.example.LOLol.entity.UserEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AuthenticationFacade {

    public Authentication getAuth()
    {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public UserEntity extractUser()
    {
        Object principal = getAuth().getPrincipal();

        log.info("extract" +principal.toString());
      //  log.info("ex2"+ (String) getAuth().getPrincipal());
        SummonerUserDetails userDetails = (SummonerUserDetails) getAuth().getPrincipal();
 //       Authentication authentication = SecurityContextHolder.getContext().getAuthentication();


        return userDetails.getEntity();
    }
}
