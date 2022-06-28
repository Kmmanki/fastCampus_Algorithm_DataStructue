package com.springtest.springtest.board;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;


@Controller
public class BoardController {

    @Autowired
    private BoardService boardService;

    @RequestMapping("/board/openBoardList.do")
    public ModelAndView openBoardList() throws Exception{
        ModelAndView mv = new ModelAndView("/board/boardList");

        List<BoardDto> list = boardService.selectBoardList();

        mv.addObject("list", list);
        return mv;
    }

    @RequestMapping("/board/openBoardWrite.do")
    public ModelAndView openBoardWrite(){
        return new ModelAndView("/board/boardWrite");
    }

    @RequestMapping("/board/insertBoard.do")
    public String boardInsert(BoardDto board) throws Exception{
        boardService.insertBoard(board);
        return "redirect:/board/openBoardList.do";
    }

    @RequestMapping("/board/openBoardDetail.do")
    public ModelAndView openBoardDetail(@RequestParam int boardIdx) throws Exception{
        ModelAndView mv = new ModelAndView("/board/BoardDetail");
        BoardDto board = boardService.selelctBoardDetail(boardIdx);
        mv.addObject("board", board);
        return mv;
    }

    @RequestMapping("/board/deleteBoard.do")
    public String deleteBoard(int boardIdx) throws Exception{
        boardService.deleteBoard(boardIdx);
        return "redirect:/board/openBoardList.do";
    }

    @RequestMapping("/board/updateBoard.do")
    public String updateBoard(BoardDto dto) throws Exception{
        boardService.updateBoard(dto);
        return "redirect:/board/openBoardList.do";
    }
}