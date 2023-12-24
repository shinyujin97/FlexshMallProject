package com.javalab.boot.domain.order_item;

import com.javalab.boot.domain.item.Item;
import com.javalab.boot.domain.order.Order;
import lombok.*;

import javax.persistence.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Order_item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id; // Order_item_id

    private int count; // 개수

    private int price; // 금액

    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinColumn(name = "order_id")
    private Order order; // 주문 연결

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item; // 아이템 연결

    public static Order_item createOrderItem(Item item, int count){

        Order_item order_item = new Order_item();
        order_item.setItem(item);
        order_item.setCount(count);
        order_item.setPrice(item.getPrice());

        return order_item;
    }
}
