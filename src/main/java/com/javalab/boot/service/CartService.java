package com.javalab.boot.service;

import com.javalab.boot.domain.cart.Cart;
import com.javalab.boot.domain.cart.CartRepository;
import com.javalab.boot.domain.cart_item.Cart_item;
import com.javalab.boot.domain.cart_item.Cart_itemRepository;
import com.javalab.boot.domain.item.Item;
import com.javalab.boot.domain.item.ItemRepository;
import com.javalab.boot.domain.user.User;
import com.javalab.boot.domain.user.UserRepository;
import com.javalab.boot.dto.CartDto;
import com.javalab.boot.dto.CartItemDto;
import com.javalab.boot.exception.OutOfMoneyException;
import com.javalab.boot.exception.OutOfStockException;
import com.javalab.boot.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

/**
 * 주석처리
 */

@Service
@Transactional
@RequiredArgsConstructor
@Log4j2
public class CartService {
    private final CartRepository cartRepository;
    private final Cart_itemRepository cart_itemRepository;
    private final UserRepository userRepository;
    private final OrderService orderService;
    private final ItemRepository itemRepository;

    public void createCart(User user){
        Cart cart = Cart.createCart(user);
        cartRepository.save(cart);
    }
    // 장바구니 생성
    @Transactional
    public void addCart(Integer id, Long itemId, int count){
        User user = userRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);

        Item item = itemRepository.findItemById(itemId);
        Cart cart = cartRepository.findByUserId(user.getId());

        // cart 가 비어있다면 생성
        if(cart == null){
            cart = Cart.createCart(user);
            cartRepository.save(cart);

            // oneToone 매핑이지만 user엔티티안의 cart도 업데이트 시켜줘야한다.
            // cart create해도 user엔티티안의 cart가 자동으로 업데이트 되지 않는다.
            user.updateUser(cart);
            userRepository.save(user);
        }
        // cart_item 생성
        Cart_item cart_item = cart_itemRepository.findByCartIdAndItemId(cart.getId(),item.getId());

        // cart_item 이 비어있다면 새로 생성
        if(cart_item == null){
            cart_item = Cart_item.createCartItem(cart,item,count);
            cart_itemRepository.save(cart_item);
            cart.setCount(cart.getCount() + 1);
        }else{
            // 비어있지 않다면 그만큼 갯수를 추가
            cart_item.addCount(count);
        }

    }
    // 장바구니 조회
    @Transactional
    public CartDto userCartView(Cart cart){
        if (cart == null) {
            throw new IllegalArgumentException("Cart는 null일 수 없습니다");
        }
        List<Cart_item> cart_items = cart_itemRepository.findAll();
        List<CartItemDto> cartItemDtoList = new ArrayList<>();
        int totalPrice = 0;
        for (Cart_item cart_item : cart_items) {
            if (cart_item.getCart().getId() == (cart.getId())) {
                CartItemDto cartItemDto = new CartItemDto();
                cartItemDto.setId(cart_item.getId());
                cartItemDto.setItemId(cart_item.getItem().getId());
                cartItemDto.setItemName(cart_item.getItem().getName());
                cartItemDto.setPrice(cart_item.getItem().getPrice());
                cartItemDto.setCount(cart_item.getCount());
                cartItemDto.setCreateDate(cart_item.getCreateDate());
                cartItemDtoList.add(cartItemDto);
                totalPrice += (cart_item.getItem().getPrice() * cart_item.getCount());
            }
        }

        CartDto cartDto = new CartDto();
        cartDto.setCartItems(cartItemDtoList);
        cartDto.setTotalPrice(totalPrice);

        return cartDto;
    }
    // 장바구니 특정 아이템 삭제
    public void cartItemDelete(Integer id, int cart_itemId){
        User user = userRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
        user.getCart().setCount(user.getCart().getCount() -1);
        cart_itemRepository.deleteById(cart_itemId);
    }
    // 장바구니 전체 아이템 삭제
    public void cartDelete(int id) {
        List<Cart_item> cart_items = cart_itemRepository.findAll(); // 전체 Cart_item조회

        // 반복문을 이용하여 접속 User의 Cart_item 만 찾아서 삭제
        for (Cart_item cart_item : cart_items) {
            if (cart_item.getCart().getUser().getId() == id) {
                cart_item.getCart().setCount(cart_item.getCart().getCount() - 1);
                cart_itemRepository.deleteById(cart_item.getId());
            }
        }
    }
    // 장바구니 결재
    @Transactional
    public void cartPaymentAndCartDelete(Integer id){
        List<Cart_item> cart_items = cart_itemRepository.findAll(); // 전체 cart_item 찾기
        List<Cart_item> userCart_items = new ArrayList<>();
        User buyer = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));

        // 반복문을 이용하여 접속 User의 Cart_item 만 찾아서 저장
        for(Cart_item cart_item : cart_items){
            if(cart_item.getCart().getUser().getId() == buyer.getId()){
                userCart_items.add(cart_item);

                // 금액 처리 및 아이템의 stock과 팔린 갯수(count) 업데이트
                processCartItem(cart_item, buyer);
            }
        }
        orderService.order(buyer,userCart_items);

        for (Cart_item cart_item : userCart_items) {
            if(cart_item.getCart().getUser().getId() == id){
                cart_item.getCart().setCount(cart_item.getCart().getCount() - 1);
                cart_itemRepository.deleteById(cart_item.getId());
            }

        }
    }

    private void processCartItem(Cart_item cart_Item, User buyer) {
        // 재고 변경
        int stock = cart_Item.getItem().getStock();
        if (stock < cart_Item.getCount()) {
            throw new OutOfStockException("상품의 재고가 부족합니다. (현재 재고 수량 : " + stock + ")");
        }

        stock = stock - cart_Item.getCount();
        cart_Item.getItem().setStock(stock);

        // 금액 처리
        Item item = cart_Item.getItem();
        User seller = item.getUser();
        int itemCount = cart_Item.getCount();
        int itemPrice = item.getPrice();
        int totalPurchaseAmount = itemCount * itemPrice;

        if (buyer.getMoney() < totalPurchaseAmount) {
            throw new OutOfMoneyException("보유 머니가 부족합니다. (현재 보유 머니: " + buyer.getMoney() + ")");
        }

        // 총 구매금액을 buyer에서 차감하고, seller에게 추가
        buyer.setMoney(buyer.getMoney() - totalPurchaseAmount);
        seller.setMoney(seller.getMoney() + totalPurchaseAmount);

        // 아이템의 팔린 갯수(count) 업데이트
        item.setCount(item.getCount() + itemCount);
    }
}
