package com.springtest.springtest.board.mapper;

import java.util.List;

import com.springtest.springtest.board.BoardDto;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BoardMapper {
    List<BoardDto> selectBoardList() throws Exception;
    String testSelect();
    void insertBoard(BoardDto dto);
    BoardDto selectBoardDetail(int idx) throws Exception;
    void updateHitCnt(int idx) throws Exception;
    void deleteBoard(int idx) throws Exception;
    void updateBoard(BoardDto dto) throws Exception;
}