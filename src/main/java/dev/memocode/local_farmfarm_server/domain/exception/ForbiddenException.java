package dev.memocode.local_farmfarm_server.domain.exception;

import lombok.Getter;

@Getter
public class ForbiddenException extends RuntimeException implements ErrorCodedException {

    private final ErrorDetail errorDetail;

    public ForbiddenException(ErrorDetail errorDetail) {
        super(errorDetail.getDefaultMessage());
        this.errorDetail = errorDetail;
    }
}
