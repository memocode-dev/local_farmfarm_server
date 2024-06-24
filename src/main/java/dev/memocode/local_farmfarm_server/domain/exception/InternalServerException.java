package dev.memocode.local_farmfarm_server.domain.exception;

import lombok.Getter;

@Getter
public class InternalServerException extends RuntimeException implements ErrorCodedException {

    private final ErrorDetail errorDetail;

    public InternalServerException(ErrorDetail errorDetail) {
        super(errorDetail.getDefaultMessage());
        this.errorDetail = errorDetail;
    }

    public InternalServerException(ErrorDetail errorDetail, Throwable cause) {
        super(errorDetail.getDefaultMessage(), cause);
        this.errorDetail = errorDetail;
    }
}
