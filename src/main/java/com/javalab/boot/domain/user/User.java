package com.javalab.boot.domain.user;

import com.javalab.boot.constant.Role;
import com.javalab.boot.domain.cart.Cart;
import com.javalab.boot.domain.item.Item;
import com.javalab.boot.domain.order.Order;
import com.javalab.boot.dto.UserDto;
import lombok.*;
import org.hibernate.annotations.BatchSize;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;


    @Column(unique = true) // username 중복 안됨
    private String username;
    private String password;
    private String name;
    private String email;
    private int money;

    //주소
    private String addr1; // 우편번호
    private String addr2;
    private String addr3;
    private String address; // 주소

    private String phoneNumber; // 핸드폰번호
    private String grade; // 등급
    @Enumerated(EnumType.STRING)
    private Role role; // 권한
    @Column(nullable = false, columnDefinition = "DATE DEFAULT CURRENT_DATE")
    private LocalDateTime createDate; // 가입 날짜

    // Item 과 연결
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @Builder.Default
    private List<Item> items = new ArrayList<>();

    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY)
    private Cart cart;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @BatchSize(size = 10)
    @Builder.Default
    private List<Order> orders = new ArrayList<>();

    @PrePersist // DB에 INSERT 되기 직전에 실행. 즉 DB에 값을 넣으면 자동으로 실행됨
    public void createDate() {
        this.createDate = LocalDateTime.now();
    }

    public void updateUser(UserDto userDto){
        this.email = userDto.getEmail();
        this.addr1 = userDto.getAddr1();
        this.addr2 = userDto.getAddr2();
        this.addr3 = userDto.getAddr3();
        this.address = userDto.getAddr2() + ' ' + userDto.getAddr3();
        this.phoneNumber = userDto.getPhoneNumber();
    }

    public static User createUser(UserDto userDto, PasswordEncoder passwordEncoder){
        String password = passwordEncoder.encode(userDto.getPassword());
        User user = User.builder()
                .username(userDto.getUsername())
                .password(password) // 비밀번호 암호화
                .name(userDto.getName())
                .email(userDto.getEmail())
                .addr1(userDto.getAddr1())
                .addr2(userDto.getAddr2())
                .addr3(userDto.getAddr3())
                .address(userDto.getAddr2() + ' ' + userDto.getAddr3())
                .phoneNumber(userDto.getPhoneNumber())
                .role(Role.ROLE_USER)
                .build();
        return user;

    }
    public static User createAdminUser(UserDto userDto, PasswordEncoder passwordEncoder) {
        String password = passwordEncoder.encode(userDto.getPassword());
        User user = User.builder()
                .username(userDto.getUsername())
                .password(password) // 비밀번호 암호화
                .name(userDto.getName())
                .email(userDto.getEmail())
                .addr1(userDto.getAddr1())
                .addr2(userDto.getAddr2())
                .addr3(userDto.getAddr3())
                .address(userDto.getAddr2() + ' ' + userDto.getAddr3())
                .phoneNumber(userDto.getPhoneNumber())
                .role(Role.ROLE_ADMIN)
                .build();
        return user;
    }
    public void updateUser(Cart cart){
        this.cart = cart;
    }

}
