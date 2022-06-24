package com.example.board.service;

import com.example.board.dto.BoardDTO;
import com.example.board.entity.BoardEntity;
import com.example.board.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    public Long save(BoardDTO boardDTO) {
        BoardEntity boardEntity = BoardEntity.toEntity(boardDTO);
        Long id = boardRepository.save(boardEntity).getId();
        return id;
    }
    public List<BoardDTO> findAll() {
        List<BoardEntity> boardEntity = boardRepository.findAll();
        List<BoardDTO> board = new ArrayList<>();
        for (BoardEntity boardList : boardEntity) {
            board.add(BoardDTO.toMemberDTO(boardList));
        }
        return board;
    }
    public BoardDTO detail(Long id) {
        Optional<BoardEntity> optionalBoardEntity = boardRepository.findById(id);
        if(optionalBoardEntity.isPresent()){
         BoardEntity boardEntity = optionalBoardEntity.get();
         BoardDTO boardDTO = BoardDTO.toMemberDTO(boardEntity);
            return boardDTO;
        }else {
            return null;
        }
    }

    public void delete(Long id) {
        boardRepository.deleteById(id);
    }
    public void update(BoardDTO boardDTO) {
        boardRepository.save(BoardEntity.toUpdateEntity(boardDTO));
    }
}
