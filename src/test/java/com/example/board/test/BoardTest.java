package com.example.board.test;

import com.example.board.common.PagingConst;
import com.example.board.dto.BoardDTO;
import com.example.board.dto.BoardMapperDTO;
import com.example.board.dto.CommentDTO;
import com.example.board.dto.MemberDTO;
import com.example.board.entity.BoardEntity;
import com.example.board.entity.CommentEntity;
import com.example.board.entity.MemberEntity;
import com.example.board.repository.BoardMapperRepository;
import com.example.board.repository.BoardRepository;
import com.example.board.repository.CommentRepository;
import com.example.board.repository.MemberRepository;
import com.example.board.service.BoardService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@SpringBootTest
public class BoardTest {
    @Autowired
    private BoardService boardService;
    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    BoardMapperRepository boardMapperRepository;




    //    public BoardDTO newBoard(int i){
//    }
    @Test
    @Transactional
    @DisplayName("페이징 테스트")
    public void pagingTest() {
        int page = 5;
        Page<BoardEntity> boardEntities = boardRepository.findAll(PageRequest.of(page, PagingConst.PAGE_LIMIT, Sort.by(org.springframework.data.domain.Sort.Direction.DESC, "id")));
        // Page 객체가 제공해주는 메서드 확인
        System.out.println("boardEntities.getContent() = " + boardEntities.getContent()); // 요청페이지에 들어있는 데이터
        System.out.println("boardEntities.getTotalElements() = " + boardEntities.getTotalElements()); // 전체 글갯수
        System.out.println("boardEntities.getNumber() = " + boardEntities.getNumber()); // 요청페이지(jpa 기준)
        System.out.println("boardEntities.getTotalPages() = " + boardEntities.getTotalPages()); // 전체 페이지 갯수
        System.out.println("boardEntities.getSize() = " + boardEntities.getSize()); // 한페이지에 보여지는 글갯수
        System.out.println("boardEntities.hasPrevious() = " + boardEntities.hasPrevious()); // 이전페이지 존재 여부
        System.out.println("boardEntities.isFirst() = " + boardEntities.isFirst()); // 첫페이지인지 여부
        System.out.println("boardEntities.isLast() = " + boardEntities.isLast()); // 마지막페이지인지 여부

        Page<BoardDTO> boardList = boardEntities.map(
                board -> new BoardDTO(board.getId(),
                        board.getBoardTitle(),
                        board.getBoardWriter(),
                        board.getBoardHits(),
                        board.getCreatedTime()
                ));

        System.out.println("boardList.getContent() = " + boardList.getContent()); // 요청페이지에 들어있는 데이터
        System.out.println("boardList.getTotalElements() = " + boardList.getTotalElements()); // 전체 글갯수
        System.out.println("boardList.getNumber() = " + boardList.getNumber()); // 요청페이지(jpa 기준)
        System.out.println("boardList.getTotalPages() = " + boardList.getTotalPages()); // 전체 페이지 갯수
        System.out.println("boardList.getSize() = " + boardList.getSize()); // 한페이지에 보여지는 글갯수
        System.out.println("boardList.hasPrevious() = " + boardList.hasPrevious()); // 이전페이지 존재 여부
        System.out.println("boardList.isFirst() = " + boardList.isFirst()); // 첫페이지인지 여부
        System.out.println("boardList.isLast() = " + boardList.isLast()); // 마지막페이지인지 여부
    }


    public BoardDTO newBoard(int i) {
        BoardDTO boardDTO = new BoardDTO("title"+ i,"writer" + i,"password" + i,"contents" + i);
        return boardDTO;
    }

    @Test
    @Transactional
    @Rollback(value = false)
    public void newMember() {
        MemberDTO memberDTO = new MemberDTO("memberEmail","memberPassword","memberName");
        memberRepository.save(MemberEntity.toSaveEntity(memberDTO));
    }

    @Test
    @Transactional
    @Rollback(value = false)
    @DisplayName("회원 게시글 연관관계 테스트")
    public void memberBoardSaveTest() {
        BoardDTO boardDTO = newBoard(1); // 저장할 게시글 객체
        // 회원 엔티티 객체를 같이 줘야하니까 위에서 저장한 이메일값으로 회원 엔티티 조회
        MemberEntity memberEntity = memberRepository.findByMemberEmail("email").get();
        // 게시글 객체와 회원 엔티티로 boardEntity 객체 생성
        BoardEntity boardEntity = BoardEntity.toEntity(boardDTO, memberEntity);
        // 저장 수행
        boardRepository.save(boardEntity);
    }


    @Test
    @Transactional // 노세션이면 뺄것 객체를 찾고 사용하는데 필요
    @Rollback(value = false)
    @DisplayName("회원 게시글 연관관계 테스트")
    public void memberBoardFindByTest() {
        // 위에서 저장한 테이블 조회
        Optional<BoardEntity> optionalBoardEntity = boardRepository.findById(1L);
        if(optionalBoardEntity.isPresent()) {
            BoardEntity boardEntity = optionalBoardEntity.get();
            System.out.println("boardEntity.getId() = " + boardEntity.getId());
            System.out.println("boardEntity.getBoardTitle() = " + boardEntity.getBoardTitle());
            // 게시글 작성자의 이름을 보고싶다.
            // select m.member_name from member_table m, board_table b where m.member_id = b.member_id where b.member_id
            // 객체 그래프 탐색
            System.out.println("boardEntity.getMemberEntity().getMemberName() = " + boardEntity.getMemberEntity().getMemberName());

        }
    }
    @Test
    @Transactional
    @Rollback(value = false)
    @DisplayName("회원 게시글 연관관계 삭제 테스트")
    public void deleteTest() {
        // 게시글 삭제
//        boardRepository.deleteById(1L);
        // 게시글을 작성한 회원 삭제
        memberRepository.deleteById(1L);
    }

    @Test
    @Transactional
    @Rollback(value = false)
    @DisplayName("댓글 작성 테스트")
    public void commentSaveTest() {
        BoardDTO boardDTO = newBoard(99); // 저장할 게시글 객체
        // 회원 엔티티 객체를 같이 줘야하니까 위에서 저장한 이메일값으로 회원 엔티티 조회
        MemberEntity memberEntity = memberRepository.findByMemberEmail("email").get();
        // 게시글 객체와 회원 엔티티로 boardEntity 객체 생성
        BoardEntity boardEntity = BoardEntity.toEntity(boardDTO, memberEntity);
        // 저장 수행 후 게시글 번호를 가져옴.
        Long saveBoardId = boardRepository.save(boardEntity).getId();
        // 댓글 저장을 위해 회원 엔티티, 게시글엔티티 준비
//        BoardEntity findBoardEntity = boardRepository.findById(saveBoardId).get();


        // 이미 저장된 게시글에 댓글만 추가
        BoardEntity findBoardEntity = boardRepository.findById(5L).get();

        // 댓글 저장을 엔티티 객체 생성
        CommentDTO commentDTO = new CommentDTO("11","1");
        CommentEntity commentEntity = CommentEntity.toSaveEntity(commentDTO, findBoardEntity, memberEntity);
        commentRepository.save(commentEntity);
    }
    
    
    @Test
    @Transactional
    @Rollback(value = false)
    @DisplayName("회원 댓글 리스트 테스트")
    public void commentListTest(){
        // 댓글이 들어있는 게시글 엔티티 조회
        Optional<BoardEntity> optionalBoardEntity = boardRepository.findById(5L);
        // 게시글 엔티티의 댓글 목록 조회
        if(optionalBoardEntity.isPresent()) {
            BoardEntity boardEntity = optionalBoardEntity.get();
            List<CommentEntity> commentEntityList = boardEntity.getCommentEntityList();
            for (CommentEntity commentEntity: commentEntityList) {
                System.out.println("commentEntity.getId() = " + commentEntity.getId());
                System.out.println("commentEntity.getCommentContents() = " + commentEntity.getCommentContents());
            }
        }
    }

    @Test
    @Transactional
    @Rollback(value = false)
    @DisplayName("검색 테스트")
    public void searchTest() {
        List<BoardDTO> boardDTOList = boardService.search("1","q2");
        System.out.println("boardDTOList = " + boardDTOList);
    }
    
    @Test
    @Transactional
    @Rollback(value = false)
    @DisplayName("mapper 테스트")
    public void mapperTest() {
        List<BoardMapperDTO> boardDTOList = boardMapperRepository.boardList();
        System.out.println("boardDTOList = " + boardDTOList);
    }

}