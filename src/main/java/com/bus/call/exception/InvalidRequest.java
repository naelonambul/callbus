package com.bus.call.exception;

import com.bus.call.domain.Heart;

public class InvalidRequest extends BoardException{

    private static final String MESSAGE = "잘못된 요청입니다.";

    public InvalidRequest(String fieldName, String message){
        super(MESSAGE);
        addValidation(fieldName, message);
    }

    public InvalidRequest(){
        super(MESSAGE);
    }
    @Override
    public int getStatusCode() {
        return 404;
    }
}
