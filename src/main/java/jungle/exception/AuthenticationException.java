package jungle.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@ResponseStatus(value = HttpStatus.UNAUTHORIZED, reason = "인증 실패")
public class AuthenticationException extends RuntimeException {
    public AuthenticationException(String message){
        super(message);
        log.error("인증 오류");
    }

}
