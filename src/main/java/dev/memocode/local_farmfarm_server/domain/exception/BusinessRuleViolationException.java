package dev.memocode.local_farmfarm_server.domain.exception;

import lombok.Getter;

@Getter
public class BusinessRuleViolationException extends RuntimeException implements ErrorCodedException {

    private final ErrorDetail errorDetail;

    public BusinessRuleViolationException(ErrorDetail errorDetail) {
        super(errorDetail.getDefaultMessage());
        this.errorDetail = errorDetail;
    }

    public BusinessRuleViolationException(ErrorDetail errorDetail, String message) {
        super(message);
        this.errorDetail = errorDetail;
    }
}
