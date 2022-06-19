package com.springtest.springtest.board;

import java.util.List;


public interface BoardService {
    
    public List<BoardDto> selectBoardList() throws Exception;
}
