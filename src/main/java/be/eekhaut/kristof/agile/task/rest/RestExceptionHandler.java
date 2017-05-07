package be.eekhaut.kristof.agile.task.rest;

import be.eekhaut.kristof.agile.task.exception.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorTO handleBusinessException(BusinessException exception) {
        return new ErrorTO("ERR_" + exception.getErrorCode().name());
    }
}