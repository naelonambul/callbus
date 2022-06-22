package com.bus.call.controller;

import com.bus.call.domain.AccountType;
import com.bus.call.domain.Board;
import com.bus.call.domain.Heart;
import com.bus.call.domain.Member;
import com.bus.call.repository.BoardRepository;
import com.bus.call.repository.HeartRepository;
import com.bus.call.repository.MemberRepository;
import com.bus.call.request.BoardCreate;
import com.bus.call.request.BoardEdit;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class BoardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void clean(){
        boardRepository.deleteAll();
        memberRepository.deleteAll();
    }

    @Test
    @DisplayName("글 작성 요청")
    void test100() throws Exception {
        Member member = Member.builder()
                .nickname("foo")
                .accountType(AccountType.REALTOR)
                .accountId("REALTOR 1")
                .build();

        memberRepository.save(member);


        BoardCreate request = BoardCreate.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .build();

        String json = objectMapper.writeValueAsString(request);

        //expected
        mockMvc.perform(post("/boards")
                        .contentType(APPLICATION_JSON)
                        .header("Authorization", member.getAuth())
                        .content(json))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(""))
                .andDo(print());
    }

    @Test
    @DisplayName("글 작성 요청 - 실패")
    void test101() throws Exception {
        Member user = Member.builder()
                .nickname("foo")
                .accountType(AccountType.REALTOR)
                .accountId("REALTOR 1")
                .build();

        memberRepository.save(user);

        BoardCreate request = BoardCreate.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .build();

        String json = objectMapper.writeValueAsString(request);

        //expected
        mockMvc.perform(post("/boardss")
                        .contentType(APPLICATION_JSON)
                        .header("Authorization", user.getAuth())
                        .content(json))
                .andExpect(status().isNotFound())
                .andExpect(content().string(""))
                .andDo(print());
    }

    @Test
    @DisplayName("글 작성 요청 - 외부인 1")
    void test103() throws Exception {
        Member user = Member.builder()
                .nickname("foo")
                .accountType(AccountType.REALTOR)
                .accountId("REALTOR 1")
                .build();

        memberRepository.save(user);

        BoardCreate request = BoardCreate.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .build();

        String json = objectMapper.writeValueAsString(request);

        //expected
        mockMvc.perform(post("/boards")
                        .contentType(APPLICATION_JSON)
                        .header(" ", user.getAuth())
                        .content(json))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    @DisplayName("글 작성 요청 - 외부인 2")
    void test104() throws Exception {
        Member user = Member.builder()
                .nickname("foo")
                .accountType(AccountType.REALTOR)
                .accountId("REALTOR 1")
                .build();

        memberRepository.save(user);

        BoardCreate request = BoardCreate.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .build();

        String json = objectMapper.writeValueAsString(request);

        //expected
        mockMvc.perform(post("/boards")
                        .contentType(APPLICATION_JSON)
                        // NULL
                        .content(json))
                .andExpect(status().isNotFound())
                .andDo(print());
    }
    @Test
    @DisplayName("요청시 DB에 값이 저장된다.")
    void test200() throws Exception {
        Member user = Member.builder()
                .nickname("foo")
                .accountType(AccountType.REALTOR)
                .accountId("REALTOR 1")
                .build();

        memberRepository.save(user);

        BoardCreate request = BoardCreate.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .build();
        String json = objectMapper.writeValueAsString(request);

        //expected
        mockMvc.perform(post("/boards")
                        .contentType(APPLICATION_JSON)
                        .header("Authorization", user.getAuth())
                        .content(json)
                )
                .andExpect(status().isOk())
                .andExpect(content().string(""))
                .andDo(print());


        //then
        assertEquals(1L, boardRepository.count());

        Board board = boardRepository.findAll().get(0);
        assertEquals("제목입니다.", board.getTitle());
        assertEquals("내용입니다.", board.getContent());
    }

    @Test
    @DisplayName("글 여러개 조회")
    public void test300() throws Exception {
        //given
        Member member = Member.builder()
                .nickname("foo")
                .accountType(AccountType.REALTOR)
                .accountId("REALTOR 1")
                .build();

        memberRepository.save(member);

        List<Board> requestBoards = IntStream.range(0, 20)
                .mapToObj(i -> Board.builder()
                        .title("foo" + i)
                        .content("bar" + i)
                        .member(member)
                        .build())
                .collect(Collectors.toList());
        boardRepository.saveAll(requestBoards);
        //expected
        mockMvc.perform(get("/boards?page=1&size=10")
                        .contentType(APPLICATION_JSON)
                        .header("Authorization", member.getAuth())
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(10)))
                .andExpect(jsonPath("$[0].title").value("foo19"))
                .andExpect(jsonPath("$[0].accountType").value("공인중개사"))
                .andDo(print());
    }

    @Test
    @DisplayName("글 여러개 조회 - 외부인")
    public void test301() throws Exception {
        //given
        Member member = Member.builder()
                .nickname("foo")
                .accountType(AccountType.REALTOR)
                .accountId("REALTOR 1")
                .build();

        memberRepository.save(member);

        List<Board> requestBoards = IntStream.range(0, 20)
                .mapToObj(i -> Board.builder()
                        .title("foo" + i)
                        .content("bar" + i)
                        .member(member)
                        .build())
                .collect(Collectors.toList());
        boardRepository.saveAll(requestBoards);
        //expected
        mockMvc.perform(get("/boards?page=1&size=10")
                        .contentType(APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(10)))
                .andExpect(jsonPath("$[0].title").value("foo19"))
                .andExpect(jsonPath("$[0].accountType").value("공인중개사"))
                .andDo(print());
    }

    @Test
    @DisplayName("글 수정")
    public void test400() throws Exception {
        //given
        Member user = Member.builder()
                .nickname("foo")
                .accountType(AccountType.REALTOR)
                .accountId("REALTOR 1")
                .build();

        memberRepository.save(user);

        Board board = Board.builder()
                .title("foo")
                .content("bar")
                .build();

        boardRepository.save(board);

        BoardEdit boardEdit = BoardEdit.builder()
                .title("foo1")
                .content("bar")
                .build();
        String json = objectMapper.writeValueAsString(boardEdit);

        //expected
        mockMvc.perform(patch("/boards/{boardId}", board.getId())
                        .contentType(APPLICATION_JSON)
                        .header("Authorization", user.getAuth())
                        .content(json))
                .andExpect(status().isOk())
                .andDo(print());

    }

    @Test
    @DisplayName("글 수정 - 외부인")
    public void test401() throws Exception {
        //given
        Member user = Member.builder()
                .nickname("foo")
                .accountType(AccountType.REALTOR)
                .accountId("REALTOR 1")
                .build();

        memberRepository.save(user);

        Board board = Board.builder()
                .title("foo")
                .content("bar")
                .build();

        boardRepository.save(board);

        BoardEdit boardEdit = BoardEdit.builder()
                .title("foo1")
                .content("bar")
                .build();
        String json = objectMapper.writeValueAsString(boardEdit);

        //expected
        mockMvc.perform(patch("/boards/{boardId}", board.getId())
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isNotFound())
                .andDo(print());
    }
    
    @Test
    @DisplayName("글 삭제")
    public void test500() throws Exception {
        //given
        Member user = Member.builder()
                .nickname("foo")
                .accountType(AccountType.REALTOR)
                .accountId("REALTOR 1")
                .build();

        memberRepository.save(user);

        Board board = Board.builder()
                .title("foo")
                .content("bar")
                .build();

        boardRepository.save(board);
        //expected
        mockMvc.perform(delete("/boards/{boardId}", board.getId())
                        .contentType(APPLICATION_JSON)
                        .header("Authorization", user.getAuth())
                )
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("글 삭제 - 외부인")
    public void test501() throws Exception {
        //given
        Member user = Member.builder()
                .nickname("foo")
                .accountType(AccountType.REALTOR)
                .accountId("REALTOR 1")
                .build();

        memberRepository.save(user);

        Board board = Board.builder()
                .title("foo")
                .content("bar")
                .build();

        boardRepository.save(board);
        //expected
        mockMvc.perform(delete("/boards/{boardId}", board.getId())
                        .contentType(APPLICATION_JSON)
                )
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Transactional
    @Test
    @DisplayName("하트 확인")
    public void test600() throws Exception {
        //given
        Member member = Member.builder()
                .nickname("foo")
                .accountType(AccountType.REALTOR)
                .accountId("REALTOR 3")
                .build();
        memberRepository.save(member);

        Board board = Board.builder()
                .title("제목입니다.3")
                .content("내용입니다.3")
                .member(member)
                .build();
        Board write = boardRepository.save(board);

        Heart heart = Heart.builder()
                .board(write)
                .member(member)
                .build();
        write.addHeart(heart);
        //expected
        mockMvc.perform(get("/boards/{boardId}", write.getId())
                        .contentType(APPLICATION_JSON)
                        .header("Authorization", member.getAuth())
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("제목입니다.3"))
                .andExpect(jsonPath("$.accountType").value("공인중개사"))
                .andExpect(jsonPath("$.heartCount").value(1))
                .andExpect(jsonPath("$.myHeart").value(true))
                .andDo(print());
    }

}