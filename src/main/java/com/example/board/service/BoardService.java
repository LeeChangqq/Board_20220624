package com.example.board.service;

import com.example.board.common.PagingConst;
import com.example.board.dto.BoardDTO;
import com.example.board.entity.BoardEntity;
import com.example.board.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    public Long save(BoardDTO boardDTO) throws IOException {
        MultipartFile boardFile = boardDTO.getBoardFile();
        String boardFileName = boardDTO.getBoardFileName();
        boardFileName = System.currentTimeMillis() + "_" + boardFileName;
        String savePath = "D:\\springboot_img\\" + boardFileName;
        if(!boardFile.isEmpty()){
            boardFile.transferTo(new File(savePath));
        }

        boardDTO.setBoardFileName(boardFileName);

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

    @Transactional
    public BoardDTO detail(Long id) {
        boardRepository.boardHits(id);
        Optional<BoardEntity> optionalBoardEntity = boardRepository.findById(id);
        // 조회수 처리
        // native sql: update board_table set boardHits=boardHits+1 where id=?
        if(optionalBoardEntity.isPresent()){
         BoardEntity boardEntity = optionalBoardEntity.get();
         BoardDTO boardDTO = BoardDTO.toMemberDTO(boardEntity);
            return boardDTO;
        }else {
            return null;
        }
    }
    public BoardDTO detail2(Long id) {
        Optional<BoardEntity> optionalBoardEntity = boardRepository.findById(id);
        // 조회수 처리
        // native sql: update board_table set boardHits=boardHits+1 where id=?
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
    public void update(BoardDTO boardDTO) throws IOException{
        MultipartFile boardFile = boardDTO.getBoardFile();
        String boardFileName = boardDTO.getBoardFileName();
        boardFileName = System.currentTimeMillis() + "_" + boardFileName;
        String savePath = "D:\\springboot_img\\" + boardFileName;
        boardFile.transferTo(new File(savePath));
        boardDTO.setBoardFileName(boardFileName);
        boardRepository.save(BoardEntity.toUpdateEntity(boardDTO));
    }
    public Page<BoardDTO> paging(Pageable pageable) {
        int page = pageable.getPageNumber(); //요청페이지값 가져옴.
        // 요청한 페이지가 1이면 페이지값을 0으로 하고 1이 아니면 요청 페이지에서 1을 뺀다.
//        page = page - 1;
        // 삼항연산자
        page = (page == 1)? 0: (page-1);
        // Page<BoardEntity> => Page<BoardDTO>
        Page<BoardEntity> boardEntities = boardRepository.findAll(PageRequest.of(page, PagingConst.PAGE_LIMIT, Sort.by(Sort.Direction.DESC, "id")));
        // BoardEntity => BoardDTO 객체 변환
        // board: BoardEntity 객체
        // new Board() 생성자
        Page<BoardDTO> boardList = boardEntities.map(
                board -> new BoardDTO(board.getId(),
                        board.getBoardTitle(),
                        board.getBoardWriter(),
                        board.getBoardHits(),
                        board.getCreatedTime()
                ));
        return boardList;
    }
}
