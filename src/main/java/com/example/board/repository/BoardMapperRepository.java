package com.example.board.repository;

import com.example.board.dto.BoardDTO;
import com.example.board.dto.BoardMapperDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BoardMapperRepository {
    List<BoardMapperDTO> boardList();

    void save(BoardMapperDTO boardMapperDTO);
}
