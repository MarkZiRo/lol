package com.example.LOLol.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

@Slf4j
public class JwtTokenFilter extends OncePerRequestFilter {
    private final JwtTokenUtils jwtTokenUtils;
    private final UserDetailsService service;

    public JwtTokenFilter(
            JwtTokenUtils jwtTokenUtils,
            UserDetailsService service
    ) {
        this.jwtTokenUtils = jwtTokenUtils;
        this.service = service;
    }
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("JwtTokenFilter: Filtering request for path: {}", request.getRequestURI());
        String jwtToken = null;

//        Cookie jwtTokenCookie = Arrays.stream(request.getCookies())
//                .filter(cookie -> cookie.getName().equals("token"))
//                .findFirst()
//                .orElse(null);

        // 헤더에서 토큰 추출
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        log.info("auth" + authorizationHeader);

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwtToken = authorizationHeader.substring(7);
            log.info("jwt" + jwtToken);

        } else if (request.getCookies() != null) { // 쿠키에서 토큰 추출
            Cookie jwtTokenCookie = Arrays.stream(request.getCookies())
                    .filter(cookie -> cookie.getName().equals("token"))
                    .findFirst()
                    .orElse(null);
            if (jwtTokenCookie != null) {
                jwtToken = jwtTokenCookie.getValue();
            }
        }

        log.info("a" + jwtToken);


        if (jwtToken != null && jwtTokenUtils.validate(jwtToken)) {

            String username = jwtTokenUtils.parseClaims(jwtToken).getSubject();

            log.info("username from parseClaims.getsubject :"+username);

            if (username != null) {

                UserDetails userDetails = service.loadUserByUsername(username);
                log.info("username from loadUserByUsername:"+userDetails);

                SecurityContext context = SecurityContextHolder.createEmptyContext();

                for (GrantedAuthority authority :userDetails.getAuthorities()) {
                    log.info("authority: {}", authority.getAuthority());
                }

                AbstractAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                jwtToken,
                                userDetails.getAuthorities()
                        );

                context.setAuthentication(authentication);
                SecurityContextHolder.setContext(context);
            }
        } else {
            log.warn("JWT Token is null or invalid");
        }

        filterChain.doFilter(request, response);
    }

//    @Override
//    protected void doFilterInternal(
//            HttpServletRequest request,
//            HttpServletResponse response,
//            FilterChain filterChain
//    ) throws ServletException, IOException {
//
//        log.debug("try jwt filter");
//        String authHeader
//                = request.getHeader(HttpHeaders.AUTHORIZATION);
//
//        if (authHeader != null && authHeader.startsWith("Bearer ")) {
//
//            String token = authHeader.split(" ")[1];
//
//            if (jwtTokenUtils.validate(token)) {
//                SecurityContext context = SecurityContextHolder.createEmptyContext();
//
//                String username = jwtTokenUtils
//                        .parseClaims(token)
//                        .getSubject();
//
//                log.info(service.toString());
//                log.info(UserDetails.class.toString());
//                log.info(service.loadUserByUsername(username).toString());
//
//                UserDetails userDetails = service.loadUserByUsername(username);
//
//                log.info(userDetails.toString());
//
//                for (GrantedAuthority authority :userDetails.getAuthorities()) {
//                    log.info("authority: {}", authority.getAuthority());
//                }
//
//                AbstractAuthenticationToken authentication =
//                        new UsernamePasswordAuthenticationToken(
//                                userDetails,
//                                token,
//                                userDetails.getAuthorities()
//                        );
//
//                context.setAuthentication(authentication);
//                SecurityContextHolder.setContext(context);
//                log.info("set security context with jwt");
//            }
//            else {
//                log.warn("jwt validation failed");
//            }
//        }
//        filterChain.doFilter(request, response);
//    }
}
