package com.javalab.boot.domain.cart;

import com.javalab.boot.domain.cart_item.Cart_item;
import com.javalab.boot.domain.user.User;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="cart_id")
    private int id;

    private int count; // 카트에 담긴 상품 갯수

    @OneToOne (fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    User user;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "cart", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Cart_item> cart_items = new ArrayList<>();

    @DateTimeFormat(pattern = "yyyy-mm-dd")
    private LocalDate createDate; // 날짜

    @PrePersist
    public void createDate(){
        this.createDate = LocalDate.now();
    }
    public static Cart createCart(User user){
        Cart cart = new Cart();
        cart.user = user;
        cart.count = 0;
        return cart;
    }

}
