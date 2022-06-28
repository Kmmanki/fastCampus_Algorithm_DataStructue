package com.springtest.springtest.board;

import java.util.List;


public interface BoardService {
    
    public List<BoardDto> selectBoardList() throws Exception;
    public void insertBoard(BoardDto dto) throws Exception;
    public BoardDto selelctBoardDetail(int idx) throws Exception;
    // public void updateHitCnt(int idx) throws Exception;
    public void deleteBoard(int idx) throws Exception;
    public void updateBoard(BoardDto dto) throws Exception;
}
