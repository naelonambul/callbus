package com.bus.call.response;

import com.bus.call.domain.Board;
import com.bus.call.domain.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class BoardResponse {
    private final Long id;
    private final String title;
    private final String content;
    private final String writer;
    private final String accountType;
    private final Long heartCount;
    private final Boolean myHeart;

    public BoardResponse(Board board) {
        this.id = board.getId();
        this.title = board.getTitle();
        this.content = board.getContent();
        this.writer = board.getMember().getNickname();
        this.accountType = board.getMember().getAccountType().name();
        this.heartCount = Long.valueOf(board.getHearts().size());
        this.myHeart = false;
    }

    @Builder
    public BoardResponse(Long id, String title, String content, String writer, String accountType, Long heartCount, Boolean myHeart) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.writer = writer;
        this.accountType = accountType;
        this.heartCount = heartCount;
        this.myHeart = myHeart;
    }

}
