package com.javalab.boot.service;

import com.javalab.boot.config.auth.PrincipalDetails;
import com.javalab.boot.domain.user.User;
import com.javalab.boot.domain.user.UserRepository;
import com.javalab.boot.dto.UserDto;
import com.javalab.boot.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class UserPageService {
    private final UserRepository userRepository;

    public User findUser(Integer id){
        return userRepository.findById(id).get();
    }

    public UserDto findUserId(Integer id){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
        UserDto userDto = UserDto.fromEntity(user);
        return userDto;
    }


    /**
     * security에 있는 user객체를 update해주는 메소드
     * 데이터가 변경된 후 화면을 요청할 때 데이터베이스에 접근하여
     * security에 있는 user객체정보를 갱신해줌.
     * 아래 메소드를 사용하지 않을 시 security에 있는 user 객체는 갱신 안됨.
     */
    public void updatePrincipalDetails(Integer userId, PrincipalDetails principalDetails) {
        User user = findUser(userId);
        principalDetails.setUser(user);
    }

    public List<User> userList(){
        return userRepository.findAll();
    }

    public void userUpdate(int id,User user){
        User tempUser = userRepository.findById(id).get();
        tempUser.setRole(user.getRole());

        userRepository.save(tempUser);
    }

    /**
     * 잔액 충전 시 기존 금액 조회 후  + 충전 금액 만큼 증가해주는 메소드
     * security 영역에 있는 로그인 객체 조회해서 초기화해줌.
     */
    public void chargePoint(int id,int amount, PrincipalDetails principalDetails){
        User user = userRepository.findById(id).get();
        user.setMoney(user.getMoney() + amount);
        principalDetails.setUser(user);
        userRepository.save(user);
    }


}
