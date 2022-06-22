package com.bus.call.request;

import com.bus.call.domain.AccountType;
import lombok.*;

@Setter
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberCheck {
    private Long id;
    private AccountType accountType;

    @Builder
    public MemberCheck(Long id, AccountType accountType) {
        this.id = id;
        this.accountType = accountType;
    }
}
