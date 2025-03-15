package be.geon.moa.moamoa.config;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum ErrorCode {

    // Common
    NOT_FOUND_CATEGORY("B001", "카테고리가 존재하지 않습니다"),
    INVALID_URL("B002", "유효하지 않은 URL 형식입니다: "),

    // Validation/
    VALIDATION_EXCEPTION("V001", "유효성 검증에 실패했습니다"),
    METHOD_NOT_ALLOWED_EXCEPTION("V002", "지원하지 않는 HTTP 메서드입니다"),
    UNSUPPORTED_MEDIA_TYPE("V003", "지원하지 않는 미디어 타입입니다"),

    // Authentication & Authorization
    UNAUTHORIZED_EXCEPTION("A001", "인증이 필요합니다"),
    FORBIDDEN_EXCEPTION("A002", "접근 권한이 없습니다"),

    // Resource
    NOT_FOUND_EXCEPTION("R001", "요청한 리소스를 찾을 수 없습니다"),
    CONFLICT_EXCEPTION("R002", "리소스 충돌이 발생했습니다"),

    // Server
    INTERNAL_SERVER_EXCEPTION("S001", "서버 내부 오류가 발생했습니다"),
    BAD_GATEWAY_EXCEPTION("S002", "외부 서비스 연동 중 오류가 발생했습니다");

    private final String code;
    private final String message;
}