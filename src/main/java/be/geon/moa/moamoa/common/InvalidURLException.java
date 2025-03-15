package be.geon.moa.moamoa.common;

import be.geon.moa.moamoa.config.ErrorCode;

public class InvalidURLException extends BookMarkAbstractException{

    public InvalidURLException(String message) {
        super(message);
    }

    @Override
    public String getStatusCode() {
        return ErrorCode.INVALID_URL.getCode();
    }
}
