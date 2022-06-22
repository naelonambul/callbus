package com.bus.call.exception;

public class BoardNotFound extends BoardException{
    private static final String MESSAGE = "존재하지 않는 글 입니다.";
    public BoardNotFound() {super(MESSAGE);}

    @Override
    public int getStatusCode() {
        return 404;
    }
}
