package com.javalab.boot.domain.board;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {

    List<Board> findAll(Sort sort);

    //쿼리문으로 순서 역정렬()
    @Query("SELECT b FROM Board b WHERE b.author IN :authors ORDER BY b.createdDate DESC")
    List<Board> findLatestByAuthors(@Param("authors") List<String> authors, Pageable pageable);

}
