package dev.memocode.local_farmfarm_server.domain.exception;

import lombok.Getter;

import static dev.memocode.local_farmfarm_server.domain.exception.ErrorCodeLogLevel.*;

@Getter
public enum BaseErrorCode implements ErrorDetail {

    PERMISSION_DENIED("권한이 부족합니다.", WARNING),
    UNAUTHENTICATED("인증이 필요합니다.", WARNING),
    INTERNAL_SERVER_ERROR("서버 에러", CRITICAL),
    VALIDATION_ERROR("서버 에러", INFO),
    ;

    private final String defaultMessage;
    private final ErrorCodeLogLevel logLevel;

    BaseErrorCode(String defaultMessage, ErrorCodeLogLevel logLevel) {
        this.defaultMessage = defaultMessage;
        this.logLevel = logLevel;
    }

    @Override
    public String getErrorCode() {
        return this.name();
    }
}
