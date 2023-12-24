package com.javalab.boot.config.auth;



import com.javalab.boot.domain.user.UserRepository;
import com.javalab.boot.domain.user.User;
import lombok.RequiredArgsConstructor;

import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;


@Service
@RequiredArgsConstructor
@Transactional
@Log4j2
public class PrincipalDetailService implements UserDetailsService {
    private final UserRepository userRepository;

    public User userCreate(User user){
        validateDuplicateUser(user);
        return userRepository.save(user);
    }

    private void validateDuplicateUser(User user){
        User findUser = userRepository.findByusername(user.getUsername());
        if(findUser != null){
            throw new IllegalStateException("이미 가입된 회원입니다.");
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{

        User user = userRepository.findByusername(username);
        log.info("user : " + user);

        if(user == null){
            throw new UsernameNotFoundException(username);
        }
        return new PrincipalDetails(user);

    }

    public boolean isUsernameAvailable(String username) {

        return !userRepository.existsByUsername(username);
    }
}
