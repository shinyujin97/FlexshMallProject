package com.javalab.boot.controller;


import com.javalab.boot.config.auth.PrincipalDetails;
import com.javalab.boot.domain.order.Order;
import com.javalab.boot.service.OrderService;
import com.javalab.boot.service.UserPageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@RequiredArgsConstructor
@Controller
@Log4j2
public class OrderController {
    private final OrderService orderService;
    private final UserPageService userPageService;
    // 특정 주문내역 수정처리
    @PostMapping("/order/update/{id}")
    public String orderUpdate(@PathVariable("id") Integer id, Order order){
        orderService.orderUpdate(id,order);

        return "redirect:/main";
    }

    // 특정 주문내역 환불처리
    @PostMapping("/order/refund/{id}")
    public String orderRefund(@PathVariable("id") Integer id, @AuthenticationPrincipal PrincipalDetails principalDetails){
            orderService.orderRefund(id,principalDetails);

        return "redirect:/main";
    }

    // 특정 주문내역 환불처리취소
    @PostMapping("/order/refundCancle/{id}")
    public String orderRefundCancle(@PathVariable("id") Integer id, Order order){
        order.setStatus("환불 취소");
        orderService.orderUpdate(id,order); // 환불처리

        return "redirect:/main";
    }
}
