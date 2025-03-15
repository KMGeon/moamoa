package be.geon.moa.moamoa.common;

import be.geon.moa.moamoa.config.ErrorCode;

public class NotFoundCategoryException extends BookMarkAbstractException{

    public NotFoundCategoryException(String message) {
        super(message);
    }

    @Override
    public String getStatusCode() {
        return ErrorCode.NOT_FOUND_CATEGORY.getCode();
    }
}
