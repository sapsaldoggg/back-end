package solobob.solobobmate.controller.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SoloBobException extends RuntimeException{

    private final ErrorCode errorCode;

}
