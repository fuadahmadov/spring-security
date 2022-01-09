package com.springsecurity.constant;

import lombok.AllArgsConstructor;

import static com.springsecurity.util.MessageResolverUtil.resolve;

@AllArgsConstructor
public enum ErrorCode {
    SERVICE_UNAVAILABLE(1, "error.serviceUnavailable"),
    UNAUTHORIZED(2, "error.unauthorized"),
    ACCESS_DENIED(3, "error.accessDenied"),
    BAD_REQUEST_PARAMS(4, "error.badRequestParams"),
    INVALID_EMAIL_PASSWORD(5, "error.invalidEmailPassword"),
    REGISTER_ALREADY_EXIST(6, "error.registerAlreadyExist");

    private final int code;
    private final String message;

    public int code() {
        return this.code;
    }

    public String message() {
        return resolve(this.message);
    }
}
