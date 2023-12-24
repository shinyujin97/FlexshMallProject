package com.javalab.boot.controller;

import com.javalab.boot.config.auth.PrincipalDetails;
import com.javalab.boot.domain.item.Item;
import com.javalab.boot.service.ItemService;
import com.javalab.boot.service.UserPageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Log4j2
public class ShopController {
    private final ItemService itemService;
    private final UserPageService userPageService;

    // 메인페이지 ( 비로그인 유저 )
    @GetMapping("/")
    public String home(Model model){
        List<Item> itemList = itemService.itemList();
        model.addAttribute("itemlist", itemList);
        return "none/main";
    }

    // 메인페이지 ( 로그인 유저 )
    @GetMapping("/main")
    @Transactional
    public String main(Model model,
                       @AuthenticationPrincipal PrincipalDetails principalDetails){


        List<Item> itemList = itemService.itemList();
        log.info("main 메소드 :");
        model.addAttribute("itemlist",itemList);

        model.addAttribute("user", principalDetails.getUser());
        return "user/main";
    }


}
