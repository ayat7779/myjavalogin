package com.mylogin.model;

/**
 * Encapsulates the response of a login attempt.
 */
public class LoginResponse {
    public enum ResultType {
        SUCCESS,
        PENDING_APPROVAL,
        REJECTED,
        INVALID_CREDENTIALS,
        DATABASE_ERROR
    }

    private final ResultType resultType;
    private final User user;
    private final String message;

    public LoginResponse(ResultType resultType, User user, String message) {
        this.resultType = resultType;
        this.user = user;
        this.message = message;
    }

    public ResultType getResultType() {
        return resultType;
    }

    public User getUser() {
        return user;
    }

    public String getMessage() {
        return message;
    }
}
