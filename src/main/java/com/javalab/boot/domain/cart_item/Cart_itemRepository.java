package com.javalab.boot.domain.cart_item;

import org.springframework.data.jpa.repository.JpaRepository;

public interface Cart_itemRepository extends JpaRepository<Cart_item, Integer> {
    Cart_item findByCartIdAndItemId(int cartId, Long itemId);
}
