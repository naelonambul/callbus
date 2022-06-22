package com.bus.call.service;

import com.bus.call.domain.AccountType;
import com.bus.call.domain.Board;
import com.bus.call.domain.Member;
import com.bus.call.repository.BoardRepository;
import com.bus.call.repository.MemberRepository;
import com.bus.call.request.BoardCreate;
import com.bus.call.request.BoardEdit;
import com.bus.call.request.BoardSearch;
import com.bus.call.response.BoardListResponse;
import com.bus.call.response.BoardResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BoardServiceTest {

    @Autowired
    private BoardService boardService;
    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    void clean(){
        boardRepository.deleteAll();
        memberRepository.deleteAll();
    }

   @Test
   @DisplayName("글 작성")
   public void test100() {
       //given
       BoardCreate boardCreate = BoardCreate.builder()
               .title("제목입니다.")
               .content("내용입니다.")
               .build();
       //when
        boardService.write(boardCreate);

       //then
       assertEquals(1L, boardRepository.count());
       Board board = boardRepository.findAll().get(0);
       assertEquals("제목입니다.", board.getTitle());
       assertEquals("내용입니다.", board.getContent());
   }

    @Test
    @DisplayName("글 작성 -맴버")
    @Transactional
    public void test101() {
        //given
        Member member = Member.builder()
                .nickname("foo")
                .accountType(AccountType.REALTOR)
                .accountId("REALTOR 1")
                .build();

        memberRepository.save(member);

        BoardCreate boardCreate = BoardCreate.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .build();
        //when
        boardService.write(boardCreate, member);

        //then
        assertEquals(1L, boardRepository.count());
        Board board = boardRepository.findAll().get(0);
        assertEquals("제목입니다.", board.getTitle());
        assertEquals("내용입니다.", board.getContent());
        assertEquals("REALTOR 1", board.getMember().getAccountId());
    }

   @Test
   @DisplayName("글 1개 조회")
   public void test200() {
       //given
       Board requestBoard = Board.builder()
               .title("foo")
               .content("bar")
               .build();
       Board save = boardRepository.save(requestBoard);
       //when
       Board board = boardService.get(save.getId());

       //then
       assertNotNull(board);
       assertEquals(1L, boardRepository.count());
       assertEquals("foo", board.getTitle());
       assertEquals("bar", board.getContent());
   }

   @Transactional
   @Test
   @DisplayName("글 1페이지 조회")
   public void test300() {
       //given
        Member member = Member.builder()
                .nickname("foo")
                .accountType(AccountType.REALTOR)
                .accountId("REALTOR 1")
                .build();

        memberRepository.save(member);

        List<Board> collect = IntStream.range(0, 20)
               .mapToObj(i -> Board.builder()
                       .title("foo" + i)
                       .content("bar" + i)
                       .member(member)
                       .build()
               ).collect(Collectors.toList());
       boardRepository.saveAll(collect);

       BoardSearch boardSearch = BoardSearch.builder()
               .page(1)
               .build();

       //expected
       List<BoardListResponse> list = boardService.getList(boardSearch);

        //then
       assertEquals(10L, list.size());
       assertEquals("foo19", list.get(0).getTitle());
   }

   @Test
   @DisplayName("글 수정 -제목")
   public void test400() {
       //given
       Board board = Board.builder()
               .title("foo")
               .content("bar")
               .build();
       boardRepository.save(board);

       BoardEdit boardEdit = BoardEdit.builder()
               .title("foo1")
               .content("bar")
               .build();

       //when
       boardService.edit(board.getId(), boardEdit);

       //then
       Board changedBoard = boardRepository.findById(board.getId())
               .orElseThrow(() -> new RuntimeException("글이 존재하지 않습니다. id=" + board.getId()));

       assertEquals("foo1", changedBoard.getTitle());
       assertEquals("bar", changedBoard.getContent());
   }

    @Test
    @DisplayName("글 수정 -내용")
    public void test401() {
        //given
        Board board = Board.builder()
                .title("foo")
                .content("bar")
                .build();
        boardRepository.save(board);

        BoardEdit boardEdit = BoardEdit.builder()
                .title("foo")
                .content("bar1")
                .build();

        //when
        boardService.edit(board.getId(), boardEdit);

        //then
        Board changedBoard = boardRepository.findById(board.getId())
                .orElseThrow(() -> new RuntimeException("글이 존재하지 않습니다. id=" + board.getId()));

        assertEquals("foo", changedBoard.getTitle());
        assertEquals("bar1", changedBoard.getContent());
    }

    @Test
    @DisplayName("글 삭제")
    public void test500() {
        //given
        Board board = Board.builder()
                .title("foo")
                .content("bar")
                .build();
        boardRepository.save(board);
        //when
        boardService.delete(board.getId());
        //then
        assertEquals(1L, boardRepository.count());
    }

    @Test
    @DisplayName("글 좋아요")
    public void test600() {
        //given
        Member member = Member.builder()
                .nickname("foo")
                .accountType(AccountType.REALTOR)
                .accountId("REALTOR 1")
                .build();

        memberRepository.save(member);

        Board board = Board.builder()
                .title("foo")
                .content("bar")
                .member(member)
                .build();
        boardRepository.save(board);
        //when
        boardService.addHeart(board.getId(), member);

        //then
        assertEquals(1L, boardRepository.count());

    }
}