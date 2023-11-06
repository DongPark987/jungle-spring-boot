package jungle.exception.ErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum UserErrorCode implements ErrorCode {

    INACTIVE_USER(HttpStatus.BAD_REQUEST, "회원을 찾을 수 없습니다."),
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "유효하지 않은 비밀 번호 입니다."),
    INVALID_USERNAME(HttpStatus.BAD_REQUEST, "유효하지 않은 유저 이름 입니다."),
    DUPLICATE_USER(HttpStatus.BAD_REQUEST, "중복된 회원입니다."),
    INVALID_TOKEN(HttpStatus.BAD_REQUEST, "토큰이 유효하지 않습니다."),
    INVALID_ACCESS(HttpStatus.BAD_REQUEST, "작성자만 삭제/수정할 수 있습니다."),
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "입력 양식이 잘못되었습니다."),
    NOT_EXIST_POST(HttpStatus.BAD_REQUEST, "없는 게시글 입니다."),
    NOT_EXIST_COMMENT(HttpStatus.BAD_REQUEST, "없는 코멘트 입니다."),

    ;

    private final HttpStatus httpStatus;
    private final String message;
}
