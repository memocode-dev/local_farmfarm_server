package dev.memocode.local_farmfarm_server.domain.exception;

public interface ErrorDetail {
    String getErrorCode();
    String getDefaultMessage();
    ErrorCodeLogLevel getLogLevel();
}
