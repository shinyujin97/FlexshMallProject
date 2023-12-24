package com.javalab.boot.domain.user;

import com.javalab.boot.constant.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByusername(String username);

    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    //롤값으로 회원 관리자 구별
    List<User> findByRole(Role role);

}
