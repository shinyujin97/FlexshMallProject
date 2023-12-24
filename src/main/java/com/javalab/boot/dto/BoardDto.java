package com.javalab.boot.dto;

import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class BoardDto {

    private Long id;

    private String title;
    private String content;
    private String author;

    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

}
