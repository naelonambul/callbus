package com.bus.call.repository;

import com.bus.call.domain.Board;
import com.bus.call.domain.QBoard;
import com.bus.call.request.BoardSearch;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class BoardRepositoryImpl implements BoardRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Board> getList(BoardSearch boardSearch) {
        return jpaQueryFactory.selectFrom(QBoard.board)
                .limit(boardSearch.getSize())
                .offset(boardSearch.getOffset())
                .orderBy(QBoard.board.id.desc())
                .fetch();
    }
}
