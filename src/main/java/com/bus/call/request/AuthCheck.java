package com.bus.call.request;

import lombok.*;

@Setter
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AuthCheck {
    private String role;
    private String id;

    @Builder
    public AuthCheck(String role, String id) {
        this.role = role;
        this.id = id;
    }
}
