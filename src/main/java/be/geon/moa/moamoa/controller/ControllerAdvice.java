package be.geon.moa.moamoa.controller;

import be.geon.moa.moamoa.common.ApiResponse;
import be.geon.moa.moamoa.common.BookMarkAbstractException;
import be.geon.moa.moamoa.common.InvalidURLException;
import be.geon.moa.moamoa.common.NotFoundCategoryException;
import be.geon.moa.moamoa.config.ErrorCode;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice
public class ControllerAdvice {

    /**
     * 북마크 관련 예외 처리
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidURLException.class)
    protected ApiResponse<Object> handleInvalidURLException(final InvalidURLException exception) {
        log.error(exception.getMessage(), exception);
        ApiResponse<Object> response = ApiResponse.error(ErrorCode.INVALID_URL, exception.getMessage());
        // 유효성 검증 결과 추가
        if (!exception.getValidation().isEmpty()) {
            // validation 정보를 response에 추가하는 로직이 필요하다면 여기에 구현
        }
        return response;
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundCategoryException.class)
    protected ApiResponse<Object> handleNotFoundCategoryException(final NotFoundCategoryException exception) {
        log.error(exception.getMessage(), exception);
        return ApiResponse.error(ErrorCode.NOT_FOUND_CATEGORY, exception.getMessage());
    }

    /**
     * 일반적인 BookMarkAbstractException 처리
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BookMarkAbstractException.class)
    protected ApiResponse<Object> handleBookMarkAbstractException(final BookMarkAbstractException exception) {
        log.error(exception.getMessage(), exception);
        // 상황에 맞는 에러 코드를 찾아 사용
        ErrorCode errorCode = findErrorCodeByStatusCode(exception.getStatusCode());
        return ApiResponse.error(errorCode, exception.getMessage());
    }

    /**
     * 400 BadRequest
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(org.springframework.validation.BindException.class)
    protected ApiResponse<Object> handleBadRequest(org.springframework.validation.BindException e) {
        log.error(e.getMessage(), e);
        return ApiResponse.error(ErrorCode.VALIDATION_EXCEPTION, Objects.requireNonNull(e.getBindingResult().getFieldError()).getDefaultMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({
            HttpMessageNotReadableException.class,
            InvalidFormatException.class,
            ServletRequestBindingException.class
    })
    protected ApiResponse<Object> handleInvalidFormatException(final Exception e) {
        log.error(e.getMessage(), e);
        return ApiResponse.error(ErrorCode.VALIDATION_EXCEPTION);
    }


    /**
     * 상태 코드로 ErrorCode 찾기
     */
    private ErrorCode findErrorCodeByStatusCode(String statusCode) {
        for (ErrorCode errorCode : ErrorCode.values()) {
            if (errorCode.getCode().equals(statusCode)) {
                return errorCode;
            }
        }
        return ErrorCode.INTERNAL_SERVER_EXCEPTION; // 기본값
    }
}