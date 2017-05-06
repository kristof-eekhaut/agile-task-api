package be.eekhaut.kristof.agile.task.exception;

public class BusinessException extends RuntimeException {

    private BusinessErrorCode errorCode;

    public BusinessException(BusinessErrorCode errorCode) {
        super();
        this.errorCode = errorCode;
    }

    public BusinessException(BusinessErrorCode errorCode, String msg) {
        super(msg);
        this.errorCode = errorCode;
    }

    public BusinessErrorCode getErrorCode() {
        return errorCode;
    }
}
