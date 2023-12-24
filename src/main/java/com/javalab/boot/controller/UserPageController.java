package com.javalab.boot.controller;

import com.javalab.boot.config.auth.PrincipalDetails;
import com.javalab.boot.constant.Role;
import com.javalab.boot.domain.cart.Cart;
import com.javalab.boot.domain.order.Order;
import com.javalab.boot.domain.order_item.Order_item;
import com.javalab.boot.domain.user.User;
import com.javalab.boot.dto.CartDto;
import com.javalab.boot.dto.RequestDto;
import com.javalab.boot.dto.UserDto;
import com.javalab.boot.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@Log4j2
public class UserPageController {

    private final UserPageService userPageService;
    private final AuthService authService;
    private final CartService cartService;
    private final ItemService itemService;
    private final OrderService orderService;
    private final MailService mailService;
    // 전역 변수
    private final RequestDto requestDto2;




    // 유저 상세페이지
    @GetMapping("/user/{id}")
    public String userPage(@PathVariable("id") Integer id, Model model,
                           @AuthenticationPrincipal PrincipalDetails principalDetails) {
        if (principalDetails.getUser().getId() == id) {
            model.addAttribute("user", userPageService.findUser(id));
            return "user/mypage";
        } else {
            return "redirect:/main";
        }
    }

    // 매출 정보 페이지
    @GetMapping("/user/{id}/saleinfo")
    public String page(@PathVariable("id") Integer id, Model model) {
        UserDto userDto = userPageService.findUserId(id);

        model.addAttribute("user", userDto);
        model.addAttribute("itemList", itemService.userItemView(userDto));


        /*// 주문 현황 데이터를 가져와 모델에 추가합니다.
        List<Object[]> orderStatusChartData = orderService.getTotalAmountByDateRange(LocalDate.now(), LocalDate.now());
        model.addAttribute("orderStatusChartData", convertToChartData(orderStatusChartData));*/

        // 오늘 총 주문 금액을 가져와 모델에 추가합니다.
        int todayTotalAmount = orderService.calculateTodayTotalAmount() - orderService.calculateTodayRefundedTotalAmount();
        model.addAttribute("todayTotalAmount", todayTotalAmount);

        // 오늘 총 주문 수량을 가져와 모델에 추가합니다.
        Integer todayTotalItemCount = orderService.getTodayTotalItemCount() - orderService.getTodayRefundedTotalItemCount();
        model.addAttribute("todayTotalItemCount", todayTotalItemCount);

        return "user/saleinfo";
    }

    // 유저 정보수정
    @GetMapping("/user/modify/{id}")
    public String userModify(@PathVariable("id") Integer id, Model model) {
        model.addAttribute("user", userPageService.findUser(id));

        return "user/useredit";
    }

    // 유저 정보수정 처리
    @PostMapping("/user/update/{id}")
    public String userUpdate(@PathVariable("id") Integer id, UserDto userDto) {
        log.info("userDto  : " + userDto); // 화면에서 받아오는 dto값 출력해보기
        authService.userUpdate(userDto);

        return "redirect:/main";
    }

    // 내가 등록한 상품조회
    @GetMapping("/user/{id}/item")
    public String myItemPage(@PathVariable("id") Integer id, Model model, @AuthenticationPrincipal PrincipalDetails principalDetails) {

        UserDto userDto = userPageService.findUserId(id);


        model.addAttribute("user", userDto);
        model.addAttribute("itemList", itemService.userItemView(userDto));
        return "user/myitem";
    }

    // 내 장바구니 조회
    @GetMapping("/user/{id}/cart")
    public String myCartPage(@PathVariable("id") Integer id, Model model, @AuthenticationPrincipal
    PrincipalDetails principalDetails) {
        // 로그인 User == 접속 User
        if (principalDetails.getUser().getId() == id) {
            // User의 장바구니를 가져온다
            Cart cart = principalDetails.getUser().getCart();
            // 장바구니의 아이템을 가져온다.
            CartDto cartDto = cartService.userCartView(cart);
            model.addAttribute("cartDto", cartDto);
            model.addAttribute("user", userPageService.findUser(id));

            return "cart/cart";
        } else {
            return "redirect:/main";
        }
    }

    // 특정 상품 장바구니에 추가
    @PostMapping("/user/{id}/cart/{itemId}")
    public String myCartAdd(@PathVariable("id") Integer id, @PathVariable("itemId")
    Long itemId, int count, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        cartService.addCart(id, itemId, count);
        // 사용자 세부 정보를 업데이트
        userPageService.updatePrincipalDetails(id,principalDetails);
        return "redirect:/item/view/{itemId}";
    }

    // 특정 상품 장바구니에서 삭제
    @GetMapping("/user/{id}/cart/{cart_itemId}/delete")
    public String myCartDelete(@PathVariable("id") Integer id,
                               @PathVariable("cart_itemId") int cart_itemId,
                               @AuthenticationPrincipal PrincipalDetails principalDetails) {
        userPageService.updatePrincipalDetails(id, principalDetails);
        cartService.cartItemDelete(id, cart_itemId);
        return "redirect:/user/{id}/cart";
    }

    // 결제 페이지
    @PostMapping("/user/{id}/cart/checkout")
    public String myCartPayment(@PathVariable("id") Integer id,
                                @ModelAttribute RequestDto requestDto,
                                @AuthenticationPrincipal PrincipalDetails principalDetails) {
        userPageService.updatePrincipalDetails(id, principalDetails);
        cartService.cartPaymentAndCartDelete(id); // 결제,장바구니 삭제처리
        // 아래 requestDto는 지역변수 이 메소드 안에서만 사용.
        log.info("requestDto : " + requestDto);
        requestDto2.updateRequestDto(requestDto);
        return "redirect:/main";
    }

    // 내 주문내역 조회
    @GetMapping("/user/{id}/order")
    public String myOrderPage(@PathVariable("id") Integer id, Model model, @AuthenticationPrincipal PrincipalDetails principalDetails) {

        // 로그인 User == 접속 User
        if (principalDetails.getUser().getId() == id) {
            // 내 주문내역 조회
            List<Order> orderList = orderService.orderList();

            // Filter orders for the specific user
            orderList = orderList.stream()
                    .filter(order -> order.getUser().getId() == id)
                    .collect(Collectors.toList());

            model.addAttribute("orderList", orderList);
            model.addAttribute("user", principalDetails.getUser());
            model.addAttribute("requestDto", requestDto2);


            return "user/order";
        } else {
            return "redirect:/main";
        }
    }

    // 내 판매현황 조회
    @Transactional
    @GetMapping("/user/{id}/sale")
    public String mySalePage(@PathVariable("id") Integer id, Model model, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        if (principalDetails.getUser().getId() == id) {
            // 판매자 정보를 받아온다.
            User seller = userPageService.findUser(id);

            List<Order> orderList = orderService.orderList();
            List<Order> mySaleList = new ArrayList<>();

            for (Order order : orderList) {
                List<Order_item> orderItemList = order.getOrder_items();

                for (Order_item order_item : orderItemList) {
                    if (seller == order_item.getItem().getUser()) {
                        mySaleList.add(order);
                        break;
                    }
                }
            }

            model.addAttribute("saleList", mySaleList);
            model.addAttribute("user", seller);

            return "user/sale";
        } else {
            return "redirect:/main";
        }
    }

    // 잔액충전
    @Transactional
    @GetMapping("/user/{id}/cash")
    public String myCash(@PathVariable("id") Integer id, Model model, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        // 로그인 User == 접속 User
        if (principalDetails.getUser().getId() == id) {
            // User의 주문내역을 가져온다.
            User user = userPageService.findUser(id);
            model.addAttribute("user", user);

            return "user/cash";
        } else {
            return "redirect:/main";
        }
    }

    // 잔액충전 처리
    @GetMapping("/user/charge/point")
    public String myCashPro(int amount, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        User user = userPageService.findUser(principalDetails.getUser().getId());
        userPageService.chargePoint(user.getId(), amount, principalDetails);
        return "redirect:/main";
    }

    // 관리자 유저관리
    @GetMapping("/user/{id}/admin")
    public String adminPage(@PathVariable("id") Integer id, Model model, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        User admin = userPageService.findUser(id);
        // User == Admin.Role 일 경우
        if (admin.getRole().equals(Role.ROLE_ADMIN)) {
            List<User> userList = userPageService.userList();
            model.addAttribute("user", admin);
            model.addAttribute("userList", userList);
            return "user/adminpage";
        } else {
            return "redirect:/main";
        }
    }


    // 관리자유저 정보수정 처리
    @PostMapping("/user/change/{id}")
    public String userChange(@PathVariable("id") Integer id, User user){
        userPageService.userUpdate(id,user);

        return "redirect:/main";
    }
}

