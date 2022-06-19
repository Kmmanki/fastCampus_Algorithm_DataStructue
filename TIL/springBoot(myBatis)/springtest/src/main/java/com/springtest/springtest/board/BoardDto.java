package com.springtest.springtest.board;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.data.annotation.CreatedDate;

import lombok.Data;

@Data
@Entity
@Table(name = "t_board")
public class BoardDto {
    @Id
    private Long boardIdx;
    private String title;
    private String contents;
    private Long hitCnt;
    private String creatorId;

    private String deleteYn;

    @CreatedDate
    private LocalDateTime createDt;
}
