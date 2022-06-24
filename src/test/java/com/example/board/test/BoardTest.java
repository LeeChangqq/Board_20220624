package com.example.board.test;

import com.example.board.dto.BoardDTO;
import com.example.board.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class BoardTest {
    @Autowired
    private BoardService boardService;
//    public BoardDTO newBoard(int i){
//    }
}
