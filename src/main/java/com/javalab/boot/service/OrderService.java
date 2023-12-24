package com.javalab.boot.service;

import com.javalab.boot.config.auth.PrincipalDetails;
import com.javalab.boot.domain.cart_item.Cart_item;
import com.javalab.boot.domain.cart_item.Cart_itemRepository;
import com.javalab.boot.domain.item.Item;
import com.javalab.boot.domain.order.Order;
import com.javalab.boot.domain.order.OrderRepository;
import com.javalab.boot.domain.order_item.Order_ItemRepository;
import com.javalab.boot.domain.order_item.Order_item;
import com.javalab.boot.domain.user.User;
import com.javalab.boot.domain.user.UserRepository;
import com.javalab.boot.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2
public class OrderService {
    private final OrderRepository orderRepository;
    private final Order_ItemRepository order_ItemRepository;
    private final UserPageService userPageService;

    // 주문 생성
    public void createOrder(User user){
        Order order = new Order();
        order.setUser(user);
        orderRepository.save(order);
    }

    // 주문 내역 추가
    public void order(User user, List<Cart_item> cart_items){
        List<Order_item> order_items = new ArrayList<>(); // 주문 내역에 추가할 아이템 리스트
        for (Cart_item cart_item : cart_items){
            Order_item order_item = Order_item.createOrderItem(cart_item.getItem(),cart_item.getCount());
            order_items.add(order_item);
        }
        Order order = Order.createOrder(user,order_items);
        order.setPrice(order.getTotalPrice());
        orderRepository.save(order);
    }
    // 정렬방향 asc
    /*// 전체 주문 내역 조회
    public List<Order> orderList(){
        return orderRepository.findAll();
    }*/

    // 전체 주문 내역 조회(정렬방향 desc)
    public List<Order> orderList() {
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        return orderRepository.findAll(sort);
    }

    // 특정 주문 조회
    public Order orderView(Integer id){
        return orderRepository.findById(id).get();
    }

    // 주문 수정
    public void orderUpdate(Integer id, Order order){
        Order tempOrder = orderRepository.findById(id).get();
        tempOrder.setStatus(order.getStatus());
        orderRepository.save(tempOrder);
    }


    // 날짜별 총 합계금액 조회
    /*public List<Object[]> getTotalAmountByDateRange(LocalDate startDate, LocalDate endDate) {
        return orderRepository.getTotalAmountByDateRange(startDate, endDate);
    }*/

    // 날짜별 총 합계 금액 조회
    public int calculateTodayTotalAmount() {
        // OrderRepository에서 쿼리를 사용하여 오늘의 총 주문 금액을 계산
        LocalDateTime today = LocalDateTime.now().truncatedTo(ChronoUnit.DAYS); // 현재 날짜의 0시 0분 0초
        LocalDateTime tomorrow = today.plusDays(1); // 다음 날의 0시 0분 0초



        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss.SSS");
        String formattedStartDate = today.format(formatter);
        String formattedEndDate = tomorrow.format(formatter);
        List<Object[]> result = orderRepository.getTotalAmountByDateRange(formattedStartDate, formattedEndDate);

        // 결과가 비어있으면 0을 반환, 그렇지 않으면 결과의 첫 번째 열을 반환 (totalAmount)
        return result.isEmpty() ? 0 : ((Number) result.get(0)[1]).intValue();
    }


    // 오늘 하루의 총 환불 금액 조회
    public int calculateTodayRefundedTotalAmount() {
        LocalDateTime today = LocalDateTime.now().truncatedTo(ChronoUnit.DAYS); // 현재 날짜의 0시 0분 0초
        LocalDateTime tomorrow = today.plusDays(1); // 다음 날의 0시 0분 0초

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss.SSS");
        String formattedStartDate = today.format(formatter);
        String formattedEndDate = tomorrow.format(formatter);

        Integer refundedTotalAmount = orderRepository.getRefundedTotalAmountByDateRange(formattedStartDate, formattedEndDate);

        // 값이 없으면 0, 있으면 리턴 처리
        return refundedTotalAmount != null ? refundedTotalAmount : 0;
    }

    // 날짜별 총 구매 수량 조회
    public Integer getTodayTotalItemCount() {
        LocalDateTime today = LocalDateTime.now();
        LocalDateTime startDate = today.toLocalDate().atStartOfDay();
        LocalDateTime endDate = today.toLocalDate().plusDays(1).atStartOfDay();

        Integer TodayTotalItemCount = orderRepository.getTotalItemCountByDateRange(startDate, endDate);

        // 값이 없으면 0, 있으면 리턴처리
        return TodayTotalItemCount != null ? TodayTotalItemCount : 0;
    }
    // 날짜별 환불처리된 총 구매수량 조회
    public int getTodayRefundedTotalItemCount() {
        LocalDateTime today = LocalDateTime.now().truncatedTo(ChronoUnit.DAYS); // 현재 날짜의 0시 0분 0초
        LocalDateTime tomorrow = today.plusDays(1); // 다음 날의 0시 0분 0초

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss.SSS");
        String formattedStartDate = today.format(formatter);
        String formattedEndDate = tomorrow.format(formatter);

        Integer refundedTotalItemCount = orderRepository.getRefundedItemCountByDateRange(formattedStartDate, formattedEndDate);

        // 값이 없으면 0, 있으면 리턴 처리
        return refundedTotalItemCount != null ? refundedTotalItemCount : 0;
    }


    @Transactional
    public void orderRefund(Integer id, PrincipalDetails principalDetails) {
        Optional<Order> orderOptional = orderRepository.findById(id);

        if (orderOptional.isPresent()) {
            Order order = orderOptional.get();
            order.setStatus("환불 완료");
            orderRepository.save(order);

            // 주문 항목들을 반복하면서 환불 처리
            for (Order_item orderItem : order.getOrder_items()) {
                Item item = orderItem.getItem();

                User buyer = orderItem.getOrder().getUser();

                int itemCount = orderItem.getCount();

                int itemPrice = orderItem.getPrice();
                int totalRefundAmount = itemCount * itemPrice;


                // 판매자에게 환불된 돈을 차감하고, 구매자에게 환불된 돈을 추가
                item.getUser().setMoney(item.getUser().getMoney() - totalRefundAmount);

                buyer.setMoney(buyer.getMoney() + totalRefundAmount);

                // 아이템의 stock과 팔린 갯수(count)을 복원
                item.changeStock(itemCount);
                item.setCount(item.getCount() - itemCount);
            }
            // 판매자의 정보를 업데이트
            User seller = principalDetails.getUser();
            userPageService.updatePrincipalDetails(seller.getId(), principalDetails);
        } else {
            // 주문이 존재하지 않을 때 처리
            log.error("Order not found with id: " + id);
        }
    }
}

