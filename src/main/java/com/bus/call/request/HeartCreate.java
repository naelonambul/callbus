package com.bus.call.request;

import com.bus.call.domain.Board;
import com.bus.call.domain.Member;
import lombok.*;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Setter
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HeartCreate {
    private Board board;
    private Member member;

    @Builder
    public HeartCreate(Board board, Member member) {
        this.board = board;
        this.member = member;
    }
}
