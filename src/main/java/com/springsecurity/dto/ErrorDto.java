package com.springsecurity.dto;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.springsecurity.constant.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorDto {
    private Integer code;
    private String message;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Map<String, String> warnings;

    public ErrorDto(ErrorCode errorCode) {
        this.code = errorCode.code();
        this.message = errorCode.message();
    }

    public ErrorDto(Map<String, String> warnings) {
        ErrorCode errorCode = ErrorCode.BAD_REQUEST_PARAMS;
        this.code = errorCode.code();
        this.message = errorCode.message();
        this.warnings = warnings;
    }


}
