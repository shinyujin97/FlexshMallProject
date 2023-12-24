package com.javalab.boot.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CartDto {
    private List<CartItemDto> cartItems;
    private int totalPrice;
    // 다른 필요한 필드들도 추가할 수 있습니다.


}
