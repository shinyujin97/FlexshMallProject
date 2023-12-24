package com.javalab.boot.controller;

import com.javalab.boot.config.auth.PrincipalDetailService;
import com.javalab.boot.config.auth.PrincipalDetails;
import com.javalab.boot.controller.auth.SignupDto;
import com.javalab.boot.domain.user.User;
import com.javalab.boot.dto.UserDto;
import com.javalab.boot.service.AuthService;
import com.javalab.boot.service.ItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

/**
 * Auth 컨트롤러
 */
@RequestMapping(value = "/user")
@Controller
@RequiredArgsConstructor
@Log4j2
public class AuthController {
    private final AuthService authService;
    private final PrincipalDetailService principalDetailService;
    private final PasswordEncoder passwordEncoder;
    


    @GetMapping(value = "/new")
    public String userForm(Model model){
        model.addAttribute("userDto", new UserDto());
        return "userForm";
    }

    @PostMapping(value = "/new")
    public String newUser(@Valid UserDto userDto, BindingResult bindingResult, Model model){

        if(bindingResult.hasErrors()){
            return "userForm";
        }
        try {
            User user = User.createUser(userDto,passwordEncoder);
            principalDetailService.userCreate(user);
        } catch (IllegalStateException e){
            model.addAttribute("errorMessage", e.getMessage());
            return "userForm";
        }

        return "redirect:/user/login";
    }

    @GetMapping(value = "/newAdmin")
    public String adminUserForm(Model model){
        model.addAttribute("userDto", new UserDto());
        return "adminUserForm";
    }

    @PostMapping(value = "/newAdmin")
    public String adminNewUser(@Valid UserDto userDto, BindingResult bindingResult, Model model){
        log.info("userDto : " + userDto);

        if(bindingResult.hasErrors()){
            return "adminUserForm";
        }

        try {
            User user = User.createAdminUser(userDto, passwordEncoder);
            log.info("user : " + user);
            principalDetailService.userCreate(user);
        } catch (IllegalStateException e){
            model.addAttribute("errorMessage", e.getMessage());
            return "adminUserForm";
        }

        return "redirect:/user/login";
    }

    @GetMapping(value = "/login")
    public String loginUser(){

        return "userLoginForm";
    }

    @GetMapping(value = "/login/error")
    public String loginError(Model model){
        model.addAttribute("loginErrorMsg", "아이디 또는 비밀번호를 확인해주세요");
        return "userLoginForm";
    }

    // 회원 중복 체크
    @GetMapping("/usernameCheck")
    public ResponseEntity<Map<String, Object>> usernameCheck(@RequestParam String username) {
        Map<String, Object> result = new HashMap<>();
        boolean isUsernameAvailable = principalDetailService.isUsernameAvailable(username);

        result.put("result", isUsernameAvailable ? "0" : "1");
        return ResponseEntity.ok(result);
    }

    // 기존 정보 가져오기
    @GetMapping("/getUserInfo")
    public ResponseEntity<UserDto> getUserInfo(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        User user = principalDetails.getUser();

        // 여기에서 사용자 정보를 DTO로 변환해서 반환하거나 필요한 작업을 수행할 수 있습니다.
        UserDto userDto = new UserDto();
        userDto.setName(user.getName());
        userDto.setPhoneNumber(user.getPhoneNumber());
        userDto.setAddr1(user.getAddr1());
        userDto.setAddr2(user.getAddr2());
        userDto.setAddr3(user.getAddr3());

        return ResponseEntity.ok(userDto);
    }

/*    @GetMapping("/signin")
    public String SigninForm(){
        return "signin";
    }
    @GetMapping("/signup")
    public String SignupForm(){
        return "signup";
    }
    @PostMapping("/signup")
    public String signup(SignupDto signupDto){
        // User에 signupDto 넣음
        User user = signupDto.toEntity();

        User userEntity = authService.signup(user);

        return "signin";
    }*/

}
