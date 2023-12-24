package com.javalab.boot.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CartItemDto {
    private int id;
    private long itemId;
    private String itemName;
    private int price;
    private int count;
    private LocalDate createDate;
    // 다른 필요한 필드들도 추가할 수 있습니다.


}