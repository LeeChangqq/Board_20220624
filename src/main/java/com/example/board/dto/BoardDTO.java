package com.example.board.dto;

import com.example.board.entity.BoardEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BoardDTO {
    private Long id;
    private String boardTitle;
    private String boardWriter;
    private String boardPassword;
    private String boardContents;
    private int boardHits;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;

    public BoardDTO(Long id, String boardTitle, String boardWriter, int boardHits, LocalDateTime createdTime) {
        this.id = id;
        this.boardTitle = boardTitle;
        this.boardWriter = boardWriter;
        this.boardHits = boardHits;
        this.createdTime = createdTime;
    }
    public BoardDTO(String boardTitle, String boardWriter, String boardPassword, String boardContents) {
        this.boardTitle = boardTitle;
        this.boardWriter = boardWriter;
        this.boardPassword = boardPassword;
        this.boardContents = boardContents;
    }

    public static BoardDTO toMemberDTO(BoardEntity boardEntity){
        BoardDTO boardDTO = new BoardDTO();
        boardDTO.setId(boardEntity.getId());
        boardDTO.setBoardTitle(boardEntity.getBoardTitle());
        boardDTO.setBoardWriter(boardEntity.getBoardWriter());
        boardDTO.setBoardPassword(boardEntity.getBoardPassword());
        boardDTO.setBoardContents(boardEntity.getBoardContents());
        boardDTO.setBoardHits(boardEntity.getBoardHits());
        boardDTO.setCreatedTime(boardEntity.getCreatedTime());
        boardDTO.setUpdatedTime(boardEntity.getUpdatedTime());
        return boardDTO;
    }
}
