package com.example.board.controller;

import com.example.board.dto.BoardDTO;
import com.example.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/board")
@Controller
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;

    @GetMapping("/save")
    public String saveForm() {
        return "boardPage/save";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute BoardDTO boardDTO) {
        boardService.save(boardDTO);
        return "redirect:/";
    }

    @GetMapping("/")
    public String findAll(Model model) {
        List<BoardDTO> boardList = boardService.findAll();
        model.addAttribute("board",boardList);
        return "boardPage/list";
    }
    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        BoardDTO boardDTO = boardService.detail(id);
        model.addAttribute("board",boardDTO);
        return "boardPage/detail";
    }
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        boardService.delete(id);
        return "redirect:/";
    }
    @GetMapping("/update/{id}")
    public String updateForm(@PathVariable Long id, Model model) {
        BoardDTO boardDTO = boardService.detail(id);
        model.addAttribute("board", boardDTO);
        return "boardPage/update";
    }
    @PostMapping("/update")
    public String update (@ModelAttribute BoardDTO boardDTO) {
        boardService.update(boardDTO);
        return "redirect:/board/"+boardDTO.getId();
    }


}
