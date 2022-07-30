package solobob.solobobmate.controller.exception;


import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 1) bean validation 오류
     * 2) type error
     */

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public List<ErrorResult> error(MethodArgumentNotValidException e) {
        List<ErrorResult> errorList = new ArrayList<>();

        String message = "";

        for (ObjectError error : e.getBindingResult().getAllErrors()) {
            message = error.getDefaultMessage();
            errorList.add(new ErrorResult(message));
        }
        return errorList;
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResult error(HttpMessageNotReadableException e) {
        return new ErrorResult("타입이 맞지 않습니다");
    }


    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResult badCredentialsException(BadCredentialsException e) {
        return new ErrorResult("로그인 정보가 일치하지 않습니다");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResult handleEntityNotFoundException(EntityNotFoundException e) {
        return new ErrorResult(e.getMessage());
    }


    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResult handleIllegalStateException(IllegalStateException e) {
        return new ErrorResult(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResult handleDropTheCodeException(SoloBobException e) {
        return new ErrorResult(e.getMessage());
    }
}
