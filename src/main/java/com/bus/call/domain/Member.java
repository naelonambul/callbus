package com.bus.call.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "Member")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(name="nickname")
    private String nickname;

    @Column(name="account_type")
    @Enumerated(EnumType.STRING)
    private AccountType accountType;

    @Column(name="account_id")
    private String accountId;

    @Column(name="quit")
    private boolean quit;

    @Builder
    public Member(String nickname, AccountType accountType, String accountId) {
        this.nickname = nickname;
        this.accountType = accountType;
        this.accountId = accountId;
        this.quit = false;
    }

    public String getAuth(){
        return accountType.name() + " " + id;
    }
}
