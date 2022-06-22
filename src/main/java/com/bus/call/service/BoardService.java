package com.bus.call.service;

import com.bus.call.TextDefault;
import com.bus.call.domain.Board;
import com.bus.call.domain.BoardEditor;
import com.bus.call.domain.Heart;
import com.bus.call.domain.Member;
import com.bus.call.exception.BoardNotFound;
import com.bus.call.exception.InvalidRequest;
import com.bus.call.repository.BoardRepository;
import com.bus.call.request.BoardCreate;
import com.bus.call.request.BoardEdit;
import com.bus.call.request.BoardSearch;
import com.bus.call.response.BoardListResponse;
import com.bus.call.response.BoardResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j(topic = "BoardService")
@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final TextDefault textDefault;

    private final Logger boardLogger = LoggerFactory.getLogger("Board");
    private final Logger heartLogger = LoggerFactory.getLogger("heart");


    public void write(BoardCreate boardCreate) {
        Board board = Board.builder()
                .title(boardCreate.getTitle())
                .content(boardCreate.getContent())
                .build();

        boardRepository.save(board);
        boardLogger.info("WRITE : id=[{}]", board.getId());
    }

    public Board write(BoardCreate boardCreate, Member member) {
        Board board = Board.builder()
                .title(boardCreate.getTitle())
                .content(boardCreate.getContent())
                .member(member)
                .build();
        Board save = boardRepository.save(board);
        boardLogger.info("WRITE : id=[{}]", board.getId());
        return save;
    }

    //외부
    public BoardResponse getResponse(Long id) {
        Board board = boardRepository.findById(id)
                .orElseThrow(BoardNotFound::new);
        if(isNotDeleted(board)) new BoardNotFound();

        return BoardResponse.builder()
                .id(board.getId())
                .title(board.getTitle())
                .content(board.getContent())
                .writer(board.getMember().getNickname())
                .accountType(textDefault.nameToHan(board.getMember().getAccountType().name()))
                .heartCount((long)board.getHearts().size())
                .myHeart(false)
                .build();
    }

    public BoardResponse getResponse(Long id, Member member) {
        Board board = boardRepository.findById(id)
                .orElseThrow(BoardNotFound::new);
        if(isNotDeleted(board)) new BoardNotFound();

        return BoardResponse.builder()
                .id(board.getId())
                .title(board.getTitle())
                .content(board.getContent())
                .writer(board.getMember().getNickname())
                .accountType(textDefault.nameToHan(board.getMember().getAccountType().name()))
                .heartCount((long)board.getHearts().size())
                .myHeart(getMyHeart(
                                board.getHearts().stream()
                                .filter(h->
                                        h.getMember().getId() == member.getId())
                                .findFirst()))
                .build();
    }

    public Board get(Long id) {
        Board board = boardRepository.findById(id)
                .orElseThrow(BoardNotFound::new);
        if(isNotDeleted(board)) new BoardNotFound();

        return board;
    }


    public Board getDeleted(Long id) {
        Board board = boardRepository.findById(id)
                .orElseThrow(BoardNotFound::new);

        return Board.builder()
                .title(board.getTitle())
                .content(board.getContent())
                .build();
    }

    //외부
    public List<BoardListResponse> getList(BoardSearch boardSearch) {
        return boardRepository.getList(boardSearch).stream()
                .filter(m-> isNotDeleted(m))
                .map(m->BoardListResponse.builder()
                            .id(m.getId())
                            .title(m.getTitle())
                            .writer(m.getMember().getNickname())
                            .accountType(textDefault.nameToHan(m.getMember().getAccountType().name()))
                            .heartCount((long)m.getHearts().size())
                            .myHeart(false)
                            .build()
                )
                .collect(Collectors.toList());
    }

    public List<BoardListResponse> getList(BoardSearch boardSearch, Member member) {
        return boardRepository.getList(boardSearch).stream()
                .filter(f-> isNotDeleted(f))
                .map(b->BoardListResponse.builder()
                            .id(b.getId())
                            .title(b.getTitle())
                            .writer(b.getMember().getNickname())
                            .accountType(textDefault.nameToHan(b.getMember().getAccountType().name()))
                            .heartCount((long)b.getHearts().size())
                            .myHeart(getMyHeart(
                                    b.getHearts().stream()
                                    .filter(h->
                                        h.getMember().getId() == member.getId())
                                    .findFirst())
                            )
                            .build()
                )
                .collect(Collectors.toList());
    }

    public boolean getMyHeart(Optional<Heart> heart){
        if(heart.isPresent()) return true;
        return false;
    }

//    public List<BoardResponse> getAllList(BoardSearch boardSearch) {
//        return boardRepository.getList(boardSearch).stream()
//                .map(BoardResponse::new)
//                .collect(Collectors.toList());
//    }
    @Transactional
    public void edit(Long id, BoardEdit boardEdit){
        Board board = boardRepository.findById(id)
                .orElseThrow(BoardNotFound::new);
        if(!isNotDeleted(board)) throw new BoardNotFound();
        BoardEditor.BoardEditorBuilder editorBuilder = board.toEditor();

        BoardEditor boardEditor = editorBuilder
                .title(boardEdit.getTitle())
                .content(boardEdit.getContent())
                .build();

        board.edit(boardEditor);
        boardLogger.info("BOARD - EDIT");
    }

    @Transactional
    public void delete(Long id) {
        Board board = boardRepository.findById(id)
                .orElseThrow(BoardNotFound::new);
        if(!isNotDeleted(board)) throw new BoardNotFound();

        board.delete();
        boardLogger.info("BOARD - DELETE");
    }
    private boolean isNotDeleted(Board m) {
        return m.getDeletedDate().isEqual(
                LocalDateTime.of(1990, 01, 01, 00, 00, 00));
    }

    @Transactional
    public void addHeart(Long boardId,  Member member){
        Board board = boardRepository.findById(boardId)
                .orElseThrow(BoardNotFound::new);
        if(!isNotDeleted(board)) throw new BoardNotFound();

        validateDuplicateHeart(board, member);

        Heart heart = Heart.builder()
                .board(board)
                .member(member)
                .build();
        board.addHeart(heart);
        heartLogger.info("ADD = BOARD=[{}] MEMBER=[{}]", board.getId(), member.getId());
    }

    public void validateDuplicateHeart(Board board, Member member){
        Optional<Heart> boolDuplicate = board.getHearts().stream()
                .filter(m ->
                        m.getMember().getId() == member.getId())
                .findAny();
        if(boolDuplicate.isPresent()) throw new InvalidRequest();
    }

}
