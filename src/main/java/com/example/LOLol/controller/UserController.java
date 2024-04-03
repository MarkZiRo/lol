package com.example.LOLol.controller;

import com.example.LOLol.dto.*;
import com.example.LOLol.entity.UserEntity;
import com.example.LOLol.jwt.JwtTokenUtils;
import com.example.LOLol.service.SummonerService;
import com.example.LOLol.service.UserService;
import com.example.LOLol.utils.AuthenticationFacade;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequestMapping("users")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;
    private final JwtTokenUtils jwtTokenUtils;
    private final AuthenticationFacade authenticationFacade;
    private final SummonerService summonerService;

//    @GetMapping("/login")
//    public ModelAndView loginForm(Model model)
//    {
//
//        ModelAndView modelAndView = new ModelAndView();
//        modelAndView.setViewName("my-profile");
//        modelAndView.addObject("response", 1);
//
//        return modelAndView;
//    }
//
//    @GetMapping("/my-profile")
//    public ModelAndView myProfile()
//    {
//
//        ModelAndView modelAndView = new ModelAndView();
//        modelAndView.setViewName("my-profile");
//        modelAndView.addObject("response", 1);
//
//        return modelAndView;
//    }


    @GetMapping("/my-profile")
    public String myProfile()
    {

//        ModelAndView modelAndView = new ModelAndView();
//        modelAndView.setViewName("my-profile");
//        modelAndView.addObject("response", 1);

        return "my-profile";
    }

    @GetMapping("/create")
    public String createForm(Model model)
    {

        model.addAttribute("createUserDto", new CreateUserDto());
        return "register-form";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute @Valid CreateUserDto dto, BindingResult bindingResult)
    {
//        if(service.checkUserDuplication(dto.getUsername()))
//        {
//            bindingResult.addError(new FieldError("dto", "username", "아이디 중복"));
//        }

        service.createUser(dto);


        return "register-success";
    }

//    @GetMapping("/login")
//    @ResponseBody
//    private JwtResponseDto issue(@RequestBody JwtRequestDto dto)
//    {
//        return service.issue(dto);
//    }

    @GetMapping("/login")
    private String issue(Model model)
    {
        model.addAttribute("jwtRequestDto", new JwtRequestDto());

        return "login-form";
    }

    @PostMapping("/login")
    private String login(@ModelAttribute JwtRequestDto jwtRequestDto, BindingResult bindingResult, HttpServletResponse response)
    {
        service.login(jwtRequestDto);

        String token = service.issue(jwtRequestDto);
//        log.info(jwtRequestDto.getUsername());
//        log.info(token);


        Cookie cookie = new Cookie("token", token);
        cookie.setHttpOnly(true);  //httponly 옵션 설정
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(60);
        response.addCookie(cookie);

//        summonerController.callSummonerName();
//        summonerController.champion();
//        summonerController.matchId();;
//        summonerController.match();
//        summonerController.tier();


        return "my-profile";
    }

    @GetMapping("/logout")
    public String logout(HttpServletResponse response) {

        Cookie cookie = new Cookie("token", null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        return "main";
    }

//    @PostMapping("/update")
//    @ResponseBody
//    public UserDto update(@RequestBody UpdateUserDto dto)
//    {
//        return service.updateUser(dto);
//    }

    @GetMapping("update")
    public String updatePasswordForm(Model model){

        model.addAttribute("UpdateUserDto", new UpdateUserDto());
        return "update-form";
    }

    @PostMapping("update")
    public String updatePassword(@Valid UpdateUserDto dto, BindingResult bindingResult) {

//        // 현재 비밀번호와 입력된 비밀번호가 일치하는지
//        if (!service.checkCurrentPassword(dto.getCurrentPassword())) {
//            bindingResult.addError(new FieldError("dto", "currentPassword", "현재 비밀번호가 올바르지 않습니다."));
//        }
//        //새 비밀번호와 비밀번호 확인이 일치하는지
//        if (!dto.getNewPassword().equals(dto.getConfirmPassword())) {
//            bindingResult.addError(new FieldError("dto", "confirmPassword", "새 비밀번호가 일치하지 않습니다."));
//        }

//        if (bindingResult.hasErrors()) {
//            return "password-update";
//        }

        service.updateUser(dto);
        summonerService.callRiotAPISummonerName();
        summonerService.callRiotAPIMatchId();
        summonerService.callRiotAPIMatch();
        summonerService.callRiotAPIMostChampion();
        summonerService.callRiotApiTier();

        return "my-profile";
    }

}
