package com.springtest.springtest.board.mapper;

import java.util.List;

import com.springtest.springtest.board.BoardDto;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BoardMapper {
    List<BoardDto> selectBoardList();
}
