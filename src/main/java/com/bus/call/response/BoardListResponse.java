package com.bus.call.response;

import com.bus.call.domain.Board;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardListResponse {
    private final Long id;
    private final String title;

    private final String writer;
    private final String accountType;

    private final Long heartCount;
    private final Boolean myHeart;

    public BoardListResponse(Board board) {
        this.id = board.getId();
        this.title = board.getTitle();
        this.writer = board.getMember().getNickname();
        this.accountType = board.getMember().getAccountType().name();
        this.heartCount = (long) board.getHearts().size();
        this.myHeart = false;
    }

    @Builder
    public BoardListResponse(Long id, String title, String writer, String accountType, Long heartCount, Boolean myHeart) {
        this.id = id;
        this.title = title;
        this.writer = writer;
        this.accountType = accountType;
        this.heartCount = heartCount;
        this.myHeart = myHeart;
    }

}
