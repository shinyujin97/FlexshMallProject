package com.javalab.boot.service;

import com.javalab.boot.constant.Role;
import com.javalab.boot.domain.board.Board;
import com.javalab.boot.domain.board.BoardRepository;
import com.javalab.boot.domain.user.User;
import com.javalab.boot.domain.user.UserRepository;
import com.javalab.boot.dto.BoardDto;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Service
@Transactional
public class BoardService {

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    // 생성자 주입
    public BoardService(BoardRepository boardRepository, UserRepository userRepository) {
        this.boardRepository = boardRepository;
        this.userRepository = userRepository;
    }

    // 게시글 생성
    @Transactional
    public Long createBoard(BoardDto boardDto) {
        Board board = new Board(boardDto.getTitle(), boardDto.getContent(), boardDto.getAuthor());
        return boardRepository.save(board).getId();
    }

    // 게시글 조회(전체)
    @Transactional(readOnly = true)
    public Page<BoardDto> getAllBoards(Pageable pageable) {
        return boardRepository.findAll(pageable)
                .map(board -> new BoardDto(board.getId(), board.getTitle(), board.getContent(),
                        board.getAuthor(), board.getCreatedDate(), board.getModifiedDate()));
    }

    //관리자 권한으로 게시글 조회
    @Transactional(readOnly = true)
    public List<BoardDto> getAdminBoards() {
        // ROLE_ADMIN 권한을 가진 사용자 조회
        List<User> admins = userRepository.findByRole(Role.ROLE_ADMIN);

        // 관리자 사용자 이름 목록 생성
        List<String> adminUsernames = admins.stream().map(User::getUsername).collect(Collectors.toList());

        // 관리자가 작성한 게시물 중 최신 2개 조회
        Pageable pageable = PageRequest.of(0, 2);
        List<Board> adminBoards = boardRepository.findLatestByAuthors(adminUsernames, pageable);

        // 조회한 게시물을 DTO로 변환
        List<BoardDto> adminBoardDtos = adminBoards.stream()
                .map(board -> new BoardDto(board.getId(), board.getTitle(), board.getContent(),
                        board.getAuthor(), board.getCreatedDate(), board.getModifiedDate()))
                .collect(Collectors.toList());

        return adminBoardDtos;
    }

    // 게시글 조회(단일)
    @Transactional(readOnly = true)
    public BoardDto getBoard(Long id) {
        Board board = boardRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다. id=" + id));
        return new BoardDto(board.getId(), board.getTitle(), board.getContent(), board.getAuthor(), board.getCreatedDate(), board.getModifiedDate());
    }

    // 게시글 수정
    @Transactional
    public Long updateBoard(Long id, BoardDto boardDto) {
        Board board = boardRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다. id=" + id));
        board.setTitle(boardDto.getTitle());
        board.setContent(boardDto.getContent());
        board.setAuthor(boardDto.getAuthor());
        LocalDateTime.now();
        return id;
    }

    // 게시글 삭제
    @Transactional
    public void deleteBoard(Long id) {
        Board board = boardRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다. id=" + id));
        boardRepository.delete(board);
    }
}
