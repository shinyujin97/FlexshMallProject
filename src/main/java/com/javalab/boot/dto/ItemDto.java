package com.javalab.boot.dto;

import com.javalab.boot.domain.item.Item;
import com.javalab.boot.domain.user.User;
import com.javalab.boot.constant.Category;
import lombok.*;
import lombok.extern.log4j.Log4j2;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Getter
@Setter
@Log4j2
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class ItemDto {
    private long id;
    @NotBlank(message = "상품 이름은 필수 입력 값입니다.")

    private Category category;

    private String name;
    @NotBlank(message = "가격은 필수 입력 값입니다.")
    private int price;
    @NotBlank(message = "상품 재고는 필수 입력 값입니다.")
    private int stock;
    private boolean isSoldout;
    private int count; // 팔린 수량
    @NotBlank(message = "상품 설명은 필수 입력 값입니다.")
    private String text;
    private String delivery; // 배송
    private String seller; // 판매자
    private String packaging; // 포장타입
    private String sales; // 판매단위
    private String weight; // 중량/용량
    private String allergy; // 알레르기정보
    private String expiration; //  유통기한
    private String notification; // 안내사항

    private String filename;
    private String filepath;

    private List<String> additionalImages;

    private int userId;

    /**
     * Dto -> Entity 변환 메소드
     */
    public static Item dtoToEntity(ItemDto itemDto){
        Item item = Item.builder()
                .category(itemDto.getCategory())
                .name(itemDto.getName())
                .price(itemDto.getPrice())
                .stock(itemDto.getStock())
                .isSoldout(itemDto.isSoldout())
                .count(itemDto.getCount())
                .text(itemDto.getText())
                .delivery(itemDto.getDelivery())
                .seller(itemDto.getSeller())
                .packaging(itemDto.getPackaging())
                .sales(itemDto.getSales())
                .weight(itemDto.getWeight())
                .allergy(itemDto.getAllergy())
                .expiration(itemDto.getExpiration())
                .notification(itemDto.getNotification())
                .filename(itemDto.getFilename())
                .filepath(itemDto.getFilepath())
                .additionalImages(itemDto.getAdditionalImages())
                .build();
        // userId가 있을 경우 User 엔터티를 생성하여 설정
        if (itemDto.getUserId() > 0) {
            User user = new User();
            user.setId(itemDto.getUserId());
            item.setUser(user);

        }

        return item;

    }

}
