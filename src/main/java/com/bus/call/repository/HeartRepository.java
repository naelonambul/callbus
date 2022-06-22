package com.bus.call.repository;

import com.bus.call.domain.Board;
import com.bus.call.domain.Heart;
import com.bus.call.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HeartRepository extends JpaRepository<Heart, Long> {
    Optional<Heart> findByMemberAndBoard(Member member, Board board);
}
