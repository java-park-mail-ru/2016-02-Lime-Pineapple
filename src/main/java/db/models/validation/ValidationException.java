package db.models.validation;

import org.jetbrains.annotations.NotNull;

public class ValidationException extends RuntimeException {
    private Long errorCode;

    @NotNull
    public Long getErrorCode() { return this.errorCode; }


    public ValidationException(String message, Long errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

}
