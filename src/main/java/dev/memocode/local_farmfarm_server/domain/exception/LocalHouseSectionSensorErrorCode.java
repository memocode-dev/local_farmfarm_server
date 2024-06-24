package dev.memocode.local_farmfarm_server.domain.exception;

import lombok.Getter;

import static dev.memocode.local_farmfarm_server.domain.exception.ErrorCodeLogLevel.INFO;

@Getter
public enum LocalHouseSectionSensorErrorCode implements ErrorDetail {

    NOT_FOUND_LOCAL_HOUSE_SECTION_SENSOR("로컬하우스동 센서를 찾을 수 없습니다.", INFO),
    ;

    private final String defaultMessage;
    private final ErrorCodeLogLevel logLevel;

    LocalHouseSectionSensorErrorCode(String defaultMessage, ErrorCodeLogLevel logLevel) {
        this.defaultMessage = defaultMessage;
        this.logLevel = logLevel;
    }

    @Override
    public String getErrorCode() {
        return this.name();
    }
}
