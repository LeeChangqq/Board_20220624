package com.example.board.controller;

import com.example.board.common.PagingConst;
import com.example.board.dto.BoardDTO;
import com.example.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
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
    public String save(@ModelAttribute BoardDTO boardDTO) throws IOException {
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
        BoardDTO boardDTO = boardService.detail2(id);
        model.addAttribute("board", boardDTO);
        return "boardPage/update";
    }
    @PostMapping("/update")
    public String update (@ModelAttribute BoardDTO boardDTO) throws IOException{
        boardService.update(boardDTO);
        return "redirect:/board/";
    }

    // /board?page=1
    // /board/3/1
    // rest api: 주소값만으로 자원을 식별 /board/10

    @GetMapping
    public String paging(@PageableDefault(page = 1) Pageable pageable, Model model) {
        Page<BoardDTO> boardList = boardService.paging(pageable); // pageable 인터페이스
        model.addAttribute("boardList", boardList);
        int startPage = (((int) (Math.ceil((double) pageable.getPageNumber() / PagingConst.BLOCK_LIMIT))) - 1) * PagingConst.BLOCK_LIMIT + 1;
        int endPage = ((startPage + PagingConst.BLOCK_LIMIT - 1) < boardList.getTotalPages()) ? startPage + PagingConst.BLOCK_LIMIT - 1 : boardList.getTotalPages();
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        return "boardPage/paging";
    }

    // 검색
    @GetMapping("/search")
    public String search(@RequestParam("q") String q1,@RequestParam String q2, Model model) {
        List<BoardDTO> search = boardService.search(q1, q2);
        model.addAttribute("searchList", search);
        return "boardPage/search";
    }

}
