package com.javalab.boot.dto;

import lombok.*;
import org.springframework.stereotype.Component;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Component
public class RequestDto {
    private String name;
    private String phoneNumber;
    private String addr1; // 우편번호
    private String addr2;
    private String addr3;
    private String address;
    private String request; // 요청사항

    public void updateRequestDto(RequestDto requestDto){
        this.name = requestDto.getName();
        this.phoneNumber = requestDto.getPhoneNumber();
        this.addr1 = requestDto.getAddr1();
        this.addr2 = requestDto.getAddr2();
        this.addr3 = requestDto.getAddr3();
        this.address = requestDto.getAddr2() + requestDto.getAddr3();
        this.request = requestDto.getRequest();
    }

}
