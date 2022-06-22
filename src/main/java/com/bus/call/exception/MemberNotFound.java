package com.bus.call.exception;

public class MemberNotFound extends BoardException{

    private static final String MESSAGE = "사용자 정보가 잘못됬습니다.";

    public MemberNotFound() {
        super(MESSAGE);
    }
    @Override
    public int getStatusCode() {
        return 401;
    }
}
