package com.bus.call;

import com.bus.call.domain.AccountType;
import com.bus.call.domain.Board;
import com.bus.call.domain.Heart;
import com.bus.call.domain.Member;
import com.bus.call.repository.BoardRepository;
import com.bus.call.repository.MemberRepository;
import com.bus.call.request.BoardCreate;
import com.bus.call.request.BoardSearch;
import com.bus.call.response.BoardResponse;
import com.bus.call.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
@RequiredArgsConstructor
public class TestDataInit {
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;

    /**
     * 회원 데이터 추가
     * test 시 주석 처리
     */

    @PostConstruct
    public void init(){
        //given
        List<Member> memberList1 = IntStream.range(0, 10)
                .mapToObj(i -> Member.builder()
                        .nickname("이름 "+i)
                        .accountType(AccountType.LESSEE)
                        .accountId("LESSEE "+i)
                        .build())
                .collect(Collectors.toList());
        memberRepository.saveAll(memberList1);

        List<Member> memberList2 = IntStream.range(10, 20)
                .mapToObj(i -> Member.builder()
                        .nickname("이름 "+i)
                        .accountType(AccountType.REALTOR)
                        .accountId("REALTOR "+i)
                        .build())
                .collect(Collectors.toList());
        memberRepository.saveAll(memberList2);

        List<Member> memberList3 = IntStream.range(20, 30)
                .mapToObj(i -> Member.builder()
                        .nickname("이름 "+i)
                        .accountType(AccountType.LESSEE)
                        .accountId("LESSEE "+i)
                        .build())
                .collect(Collectors.toList());
        memberRepository.saveAll(memberList3);

        List<Board> requestBoards1 = IntStream.range(0, 10)
                .mapToObj(i -> Board.builder()
                        .title("제목입니다." + i)
                        .content("내용입니다." + i)
                        .member(memberList1.get(i))
                        .build())
                .collect(Collectors.toList());
        boardRepository.saveAll(requestBoards1);

        List<Board> requestBoards2 = IntStream.range(0, 10)
                .mapToObj(i -> Board.builder()
                        .title("제목입니다." + i)
                        .content("내용입니다." + i)
                        .member(memberList2.get(i))
                        .build())
                .collect(Collectors.toList());
        boardRepository.saveAll(requestBoards2);

        List<Board> requestBoards3 = IntStream.range(0, 10)
                .mapToObj(i -> Board.builder()
                        .title("제목입니다." + i)
                        .content("내용입니다." + i)
                        .member(memberList3.get(i))
                        .build())
                .collect(Collectors.toList());
        boardRepository.saveAll(requestBoards3);

    }
}
