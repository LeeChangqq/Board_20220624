package com.example.board.entity;

import com.example.board.dto.BoardDTO;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@Table(name = "board_table")
public class BoardEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long id;

    @Column(length = 50, nullable = false)
    private String boardTitle;

    @Column(length = 20, nullable = false)
    private String boardWriter;

    @Column(length = 20, nullable = false)
    private String boardPassword;

    @Column(length = 500, nullable = false)
    private String boardContents;

    @Column
    private int boardHits;

    @Column
    private String boardFileName;


    // 회원-게시글 연관관계
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private MemberEntity memberEntity;

    // 게시글-댓글 연관관계(1:N)
    @OneToMany(mappedBy = "boardEntity", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<CommentEntity> commentEntityList = new ArrayList<>();


    // 회원과 연관관계 맺은 후
    public static BoardEntity toEntity(BoardDTO boardDTO, MemberEntity memberEntity) {
        BoardEntity boardEntity = new BoardEntity();
        boardEntity.setBoardTitle(boardDTO.getBoardTitle());
//        boardEntity.setBoardWriter(boardDTO.getBoardWriter()); // 회원 이메일을 작성자로 한다면 밑에처럼
        boardEntity.setBoardWriter(memberEntity.getMemberEmail());


        boardEntity.setBoardPassword(boardDTO.getBoardPassword());
        boardEntity.setBoardContents(boardDTO.getBoardContents());
        boardEntity.setBoardHits(0);
        boardEntity.setBoardFileName(boardDTO.getBoardFileName());
        boardEntity.setMemberEntity(memberEntity);
        return boardEntity;
    }




//    public static BoardEntity toEntity(BoardDTO boardDTO) {
//        BoardEntity boardEntity = new BoardEntity();
//        boardEntity.setBoardTitle(boardDTO.getBoardTitle());
//        boardEntity.setBoardWriter(boardDTO.getBoardWriter());
//        boardEntity.setBoardPassword(boardDTO.getBoardPassword());
//        boardEntity.setBoardContents(boardDTO.getBoardContents());
//        boardEntity.setBoardHits(0);
//        boardEntity.setBoardFileName(boardDTO.getBoardFileName());
//        return boardEntity;
//    }

    public static BoardEntity toUpdateEntity(BoardDTO boardDTO) {
        BoardEntity boardEntity = new BoardEntity();
        boardEntity.setId(boardDTO.getId());
        boardEntity.setBoardTitle(boardDTO.getBoardTitle());
        boardEntity.setBoardWriter(boardDTO.getBoardWriter());
        boardEntity.setBoardPassword(boardDTO.getBoardPassword());
        boardEntity.setBoardContents(boardDTO.getBoardContents());
        boardEntity.setBoardHits(boardDTO.getBoardHits());
        boardEntity.setBoardFileName(boardDTO.getBoardFileName());
        return boardEntity;
    }

//    public static BoardEntity toHitsEntity(BoardDTO boardDTO) {
//        int a = 1;
//        BoardEntity boardEntity = new BoardEntity();
//        boardEntity.setId(boardDTO.getId());
//        boardEntity.setBoardTitle(boardDTO.getBoardTitle());
//        boardEntity.setBoardWriter(boardDTO.getBoardWriter());
//        boardEntity.setBoardPassword(boardDTO.getBoardPassword());
//        boardEntity.setBoardContents(boardDTO.getBoardContents());
//        boardEntity.setBoardHits(a);
//        return boardEntity;
//    }
}
