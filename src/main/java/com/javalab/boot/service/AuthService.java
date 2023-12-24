package com.javalab.boot.service;

import com.javalab.boot.constant.Role;
import com.javalab.boot.domain.user.User;
import com.javalab.boot.domain.user.UserRepository;
import com.javalab.boot.dto.UserDto;
import com.javalab.boot.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
@Log4j2
public class AuthService {
    private final UserRepository userRepository;
    //private final CartService cartService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Transactional // Write(Insert, Update, Delete)
    public User signup(User user){
        String rawPassword = user.getPassword();
        String encPassword = bCryptPasswordEncoder.encode(rawPassword);
        user.setPassword(encPassword);
        user.setRole(Role.ROLE_USER); // 기본 user 권한
        user.createDate();
        User userEntity = userRepository.save(user);
        return userEntity;
    }
    @Transactional // Write(Insert, Update, Delete)
    public void userUpdate(UserDto userDto){

        User user = userRepository.findById(userDto.getId())
                .orElseThrow(() -> new UserNotFoundException("User not found with username: " + userDto.getId()));

        user.updateUser(userDto);
        userRepository.save(user);
    }
}
