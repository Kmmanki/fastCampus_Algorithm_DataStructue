package com.springtest.springtest.board;

import java.util.List;

import com.springtest.springtest.board.mapper.BoardMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BoardServiceImpl implements BoardService {

    @Autowired
    private BoardMapper boardMapper;


    @Override
    public List<BoardDto> selectBoardList()  throws Exception{
        return boardMapper.selectBoardList();
    }
    
}
