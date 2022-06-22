package com.bus.call.repository;

import com.bus.call.domain.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface BoardRepository extends JpaRepository<Board, Long>, BoardRepositoryCustom {
}
