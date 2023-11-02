package jungle.Domain.Post.Dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class PostForm {

    private  Long id;



    @NotEmpty(message = "회원 이름은 필수 입니다.")
    private String name;
    @NotEmpty(message = "회원 비밀 번호는 필수 입니다.")
    private String pw;
    @NotEmpty(message = "게시글 제목은 필수 입니다.")
    private String title;
    private String content;
}
