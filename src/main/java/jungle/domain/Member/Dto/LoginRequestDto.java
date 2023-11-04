package jungle.domain.Member.Dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
public class LoginRequestDto {

    @NotEmpty(message = "회원 이름은 필수 입니다.")
    @Pattern(regexp = "^[a-z0-9]{4,10}$",message = "최소 4자 이상, 10자 이하이며 알파벳 소문자(a~z), 숫자(0~9)인 유저명 입력바람")
    private String username;

    @NotEmpty(message = "회원 패스워드는 필수 입니다.")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\\\d)(?=.*[@$!%*?&])[A-Za-z\\\\d@$!%*?&]{8,15}$\n",message = "최소 8자 이상, 15자 이하이며 알파벳 대소문자(a~z, A~Z), 숫자(0~9), 특수문자로 이루어진 비밀번호 작성바람")
    private String password;

    public LoginRequestDto(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
