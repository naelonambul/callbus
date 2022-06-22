package com.bus.call.service;

import com.bus.call.domain.Member;
import com.bus.call.exception.MemberNotFound;
import com.bus.call.repository.MemberRepository;
import com.bus.call.request.MemberCheck;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    public Member get(MemberCheck member){
        Member findMember = memberRepository.findById(member.getId())
                .orElseThrow(MemberNotFound::new);

        return findMember;
    }

    public Optional<Member> getCheck(MemberCheck member){
        return memberRepository.findById(member.getId());
    }
}
