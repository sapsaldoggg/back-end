package solobob.solobobmate.controller.exception;


import com.sun.mail.smtp.SMTPAddressFailedException;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.mail.MailSendException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.mail.SendFailedException;
import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 1) bean validation 오류
     * 2) type error
     */


    @Data
    @AllArgsConstructor
    static class ErrorResponse<T>{
        int code;
        T message;
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        Set<String> errorList = new HashSet<>();

        for (ObjectError error : e.getBindingResult().getAllErrors()) {
            errorList.add(error.getDefaultMessage());
        }

        return new ErrorResponse(1000, errorList);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        return new ErrorResponse(1001, "타입이 맞지 않습니다");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleSendFailedException(MailSendException e) {
        return new ErrorResponse(1002, "유효하지 않은 이메일입니다");
    }


    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBadCredentialsException(BadCredentialsException e) {
        return new ErrorResponse(1003, "로그인 정보가 일치하지 않습니다");
    }


    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResult handleDropTheCodeException(SoloBobException e) {
        return new ErrorResult(e.getErrorCode().getCode(), e.getErrorCode().getMessages());
    }
}
