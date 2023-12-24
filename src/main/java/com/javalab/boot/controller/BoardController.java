package com.javalab.boot.controller;

import com.javalab.boot.config.auth.PrincipalDetails;
import com.javalab.boot.constant.Role;
import com.javalab.boot.domain.user.User;
import com.javalab.boot.domain.user.UserRepository;
import com.javalab.boot.dto.BoardDto;
import com.javalab.boot.service.BoardService;
import com.javalab.boot.service.UserPageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/board")
@Log4j2
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;
    private final UserRepository userRepository;
    private final UserPageService userPageService;


    // 게시물 목록
    @GetMapping("/list")
    public String boardList(Model model, @AuthenticationPrincipal PrincipalDetails principalDetails,
                            @PageableDefault(size = 10, sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable) {
        int id = principalDetails.getUser().getId();
        Page<BoardDto> boardList = boardService.getAllBoards(pageable);

        // 관리자가 작성한 게시글 조회
        List<BoardDto> adminBoards = boardService.getAdminBoards();

        // ROLE_ADMIN 권한을 가진 사용자의 이름 목록 조회
        List<String> adminUsernames = userRepository.findByRole(Role.ROLE_ADMIN)
                .stream()
                .map(User::getUsername)
                .collect(Collectors.toList());

        model.addAttribute("user", userPageService.findUser(id));
        model.addAttribute("boardList", boardList);

        // 관리자가 작성한 게시글 및 관리자 이름 목록 모델에 추가
        model.addAttribute("adminBoards", adminBoards);
        model.addAttribute("adminUsernames", adminUsernames);

        return "board/list";
    }

    //게시물 상세보기
    @GetMapping("/read/{id}")
    public String boardDetail(@PathVariable Long id, Model model, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        int userId = principalDetails.getUser().getId();
        BoardDto boardDto = boardService.getBoard(id);
        model.addAttribute("user", userPageService.findUser(userId));
        model.addAttribute("board", boardDto);
        return "board/read";
    }

    //글 생성 폼
    @GetMapping("/create")
    public String boardCreateForm(Model model, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        int userId = principalDetails.getUser().getId();
        model.addAttribute("boardDTO", new BoardDto());
        model.addAttribute("user", userPageService.findUser(userId));
        return "board/create";
    }

    //글 생성 처리
    @PostMapping("/create")
    public String boardCreate(@ModelAttribute BoardDto boardDto, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        int userId = principalDetails.getUser().getId();
        boardDto.setAuthor(userPageService.findUser(userId).getUsername());
        boardService.createBoard(boardDto);
        return "redirect:/board/list";
    }

    //수정 폼
    @GetMapping("/edit/{id}")
    public String boardEditForm(@PathVariable Long id, Model model, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        int userId = principalDetails.getUser().getId();
        BoardDto boardDto = boardService.getBoard(id);
        model.addAttribute("user", userPageService.findUser(userId));
        model.addAttribute("board", boardDto);
        return "board/edit";
    }


    // 수정 처리
    @PostMapping("/edit/{id}")
    public String boardEdit(@PathVariable Long id, @ModelAttribute BoardDto boardDto, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        int userId = principalDetails.getUser().getId();
        boardService.updateBoard(id, boardDto);
        return "redirect:/board/read/" + id;
    }

    // 게시글 삭제
    @GetMapping("/delete/{id}")
    public String deleteBoardGet(@PathVariable Long id, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        int userId = principalDetails.getUser().getId();
        log.info("id : " + id);
        boardService.deleteBoard(id);
        return "redirect:/board/list";
    }

}



