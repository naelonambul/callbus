package com.bus.call.repository;

import com.bus.call.domain.Board;
import com.bus.call.request.BoardSearch;

import java.util.List;

public interface BoardRepositoryCustom {
    List<Board> getList(BoardSearch boardSearch);
}
