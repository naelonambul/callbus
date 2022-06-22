package com.bus.call.controller;

import com.bus.call.domain.AccountType;
import com.bus.call.domain.Member;
import com.bus.call.exception.InvalidRequest;
import com.bus.call.request.BoardCreate;
import com.bus.call.request.BoardEdit;
import com.bus.call.request.BoardSearch;
import com.bus.call.request.MemberCheck;
import com.bus.call.response.BoardListResponse;
import com.bus.call.response.BoardResponse;
import com.bus.call.service.BoardService;
import com.bus.call.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;
    private final MemberService memberService;

    @PostMapping("/boards")
    public void write(@RequestBody @Valid BoardCreate boardCreate,
                    HttpServletRequest httpServletRequest){
        MemberCheck memberCheck = makeMemberCheck(httpServletRequest);
        if(memberCheck == null) throw new InvalidRequest();

        Member member = memberService.get(memberCheck);
        boardService.write(boardCreate, member);
    }

    @GetMapping("/boards/{boardId}")
    public BoardResponse get(@PathVariable Long boardId,
                             HttpServletRequest httpServletRequest){
        MemberCheck memberCheck = makeMemberCheck(httpServletRequest);
        if(memberCheck == null) return boardService.getResponse(boardId);

        Member member = memberService.get(memberCheck);
        return boardService.getResponse(boardId, member);
    }

    @GetMapping("/boards")
    public List<BoardListResponse> getList(@ModelAttribute BoardSearch boardSearch,
                                            HttpServletRequest httpServletRequest){
        MemberCheck memberCheck = makeMemberCheck(httpServletRequest);
        if(memberCheck == null) return boardService.getList(boardSearch);

        Member member = memberService.get(memberCheck);
        return boardService.getList(boardSearch, member);
    }

    @PatchMapping("/boards/{boardId}")
    public void edit(@PathVariable Long boardId,
                     @RequestBody @Valid BoardEdit request,
                     HttpServletRequest httpServletRequest){
        MemberCheck memberCheck = makeMemberCheck(httpServletRequest);
        if(memberCheck == null) throw new InvalidRequest();

        memberService.get(memberCheck);
        boardService.edit(boardId, request);
    }

    @DeleteMapping("/boards/{boardId}")
    public void delete(@PathVariable Long boardId,
                       HttpServletRequest httpServletRequest){
        MemberCheck memberCheck = makeMemberCheck(httpServletRequest);
        if(memberCheck == null) throw new InvalidRequest();

        memberService.get(memberCheck);
        boardService.delete(boardId);
    }

    @PostMapping("/boards/{boardId}/heart")
    public void heart(@PathVariable Long boardId,
                      HttpServletRequest httpServletRequest){
        MemberCheck memberCheck = makeMemberCheck(httpServletRequest);
        if(memberCheck == null) throw new InvalidRequest();

        Member member = memberService.get(memberCheck);
        boardService.addHeart(boardId, member);
    }


    private MemberCheck makeMemberCheck(HttpServletRequest httpServletRequest){
        String accountType = httpServletRequest.getAttribute("accountType").toString();
        if(accountType == "guest") return null;

        String id = httpServletRequest.getAttribute("member_id").toString();
        return MemberCheck.builder()
                .id(Long.valueOf(id))
                .accountType(AccountType.valueOf(accountType))
                .build();
    }
}
