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
        System.out.println( boardMapper.testSelect());
        return boardMapper.selectBoardList();
    }

    @Override
    public void insertBoard(BoardDto dto) throws Exception {
        dto.setDeleteYn("N");
        boardMapper.insertBoard(dto);

    }


    @Override
    public BoardDto selelctBoardDetail(int idx) throws Exception {
        boardMapper.updateHitCnt(idx);
        return boardMapper.selectBoardDetail(idx);

    }

    @Override
    public void deleteBoard(int idx) throws Exception {
        boardMapper.deleteBoard(idx);

    }

    @Override
    public void updateBoard(BoardDto dto) throws Exception {
        boardMapper.updateBoard(dto);
    }

}