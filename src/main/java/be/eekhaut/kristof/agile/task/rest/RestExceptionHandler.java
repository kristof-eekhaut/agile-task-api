package be.eekhaut.kristof.agile.task.rest;

import org.springframework.data.rest.core.RepositoryConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(RepositoryConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public List<ErrorTO> handleAccessDeniedException(RepositoryConstraintViolationException exception) {

        return exception.getErrors().getAllErrors().stream()
                .map(RestExceptionHandler::mapToErrorTO)
                .collect(Collectors.toList());
    }

    private static ErrorTO mapToErrorTO(ObjectError objectError) {
        if(objectError instanceof FieldError) {
            FieldError fieldError = (FieldError)objectError;
            return new ErrorTO(fieldError.getCode(), fieldError.getField());
        }
        return new ErrorTO(objectError.getCode());
    }
}