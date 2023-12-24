package com.javalab.boot.controller;

import com.javalab.boot.config.auth.PrincipalDetails;
import com.javalab.boot.constant.Role;
import com.javalab.boot.domain.item.Item;
import com.javalab.boot.dto.ItemDto;
import com.javalab.boot.service.ItemService;
import com.javalab.boot.service.UserPageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


/*
 깃허브에서 주석 추가
*/

@Controller
@RequiredArgsConstructor
@Log4j2
public class ItemController {
    private final ItemService itemService;
    private final UserPageService userPageService;


    // 상품 등록 페이지
    @GetMapping("/item/write")
    public String itemWriteForm(Model model, @AuthenticationPrincipal PrincipalDetails principalDetails){
        int id = principalDetails.getUser().getId();
        model.addAttribute("user", userPageService.findUser(id));
        return "user/itemwrite";
    }

    // 상품 등록 처리
    @PostMapping("/item/writting")
    public String itemWritting(@ModelAttribute ItemDto itemDto, @RequestParam("file") MultipartFile file, @RequestParam("additionalImagesList") List<MultipartFile> additionalImagesList,
                               @AuthenticationPrincipal PrincipalDetails principalDetails) {

        try {
            if (principalDetails != null && principalDetails.getUser() != null) {
                if (principalDetails.getUser().getRole().equals(Role.ROLE_ADMIN)) {
                    itemDto.setUserId(principalDetails.getUser().getId());
                    itemService.save(itemDto, file , additionalImagesList);
                    return "redirect:/main";
                } else {
                    // 권한이 없는 사용자의 경우에 대한 처리
                    return "redirect:/main";
                }
            } else {
                // 사용자 정보가 없는 경우에 대한 처리
                return "redirect:/main";
            }
        } catch (Exception e) {
            // 예외 발생 시의 처리
            e.printStackTrace(); // 실제 프로젝트에서는 로깅을 사용하는 것이 좋습니다.
            return "redirect:/main";
        }
    }

    // 특정 상품 정보페이지 (비로그인 / 로그인 구분)
    @GetMapping("/item/view/{id}")
    public String itemView(@PathVariable Long id, Model model,
                           @AuthenticationPrincipal PrincipalDetails principalDetails){

        if(principalDetails == null){
            ItemDto itemDto = itemService.itemView(id);
            model.addAttribute("item", itemDto);
            return "none/itemview";
        }else{
            ItemDto itemDto = itemService.itemView(id);

            model.addAttribute("user", principalDetails.getUser());

            model.addAttribute("item", itemDto);
            return "user/itemview";
        }
    }
    // 특정 상품정보 수정
    @GetMapping("/item/modify/{id}")
    public String itemModify(@PathVariable("id") Long id, Model model){
        model.addAttribute("item",itemService.itemView(id));

        return "user/itemmodify";
    }

    // 특정 상품정보 수정처리
    @PostMapping("/item/update/{id}")
    public String itemUpdate(@PathVariable("id") Long id, ItemDto itemDto, @RequestParam("file") MultipartFile file,
                             @RequestParam("additionalImagesList") List<MultipartFile> additionalImagesList)
            throws Exception{
        itemService.itemModify(itemDto,id,file, additionalImagesList);

        return "redirect:/main";
    }

    // 특정 상품 삭제
    @GetMapping("/item/delete")
    public String itemDelete(Long id){
        itemService.itemDelete(id);
        return "redirect:/main";
    }
}
