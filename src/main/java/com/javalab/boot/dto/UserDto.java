package com.javalab.boot.dto;

import com.javalab.boot.domain.user.User;
import lombok.*;

import javax.validation.constraints.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class UserDto {
    private int id;

    @NotBlank(message = "아이디를 입력해주세요.")
    private String username;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    @Size(min = 4, max = 20, message = "비밀번호는 4자 이상 20자 이하로 입력해주세요.")
    private String password;

    @NotBlank(message = "닉네임을 입력해주세요.")
    @Size(min = 2, max = 10, message = "닉네임은 2자 이상 10자 이하로 입력해주세요.")
    private String name;

    @NotBlank(message = "이메일 주소를 입력해주세요.")
    @Email(message = "올바른 이메일 주소를 입력해주세요.")
    private String email;

    @NotEmpty(message = "우편번호는 필수 입력 값입니다.")
    private String addr1; // 우편번호
    private String addr2;
    private String addr3;
    private String address;

    @NotBlank(message = "휴대폰 번호를 입력해주세요.")
    @Pattern(regexp = "(01[016789])(\\d{3,4})(\\d{4})", message = "올바른 휴대폰 번호를 입력해주세요.")
    private String phoneNumber;
    private int money;

    public static UserDto fromEntity(User user) {
        return UserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .name(user.getName())
                .email(user.getEmail())
                .addr1(user.getAddr1())
                .addr2(user.getAddr2())
                .addr3(user.getAddr3())
                .address(user.getAddr2() + ' ' + user.getAddr3())
                .phoneNumber(user.getPhoneNumber())
                .money(user.getMoney())
                .build();
    }

    // Getters and Setters
}
