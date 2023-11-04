package jungle.domain.Post.Dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostDeleteDto {

    @NotNull(message = "게시글 아이디는 필수 입니다.")
    private long id;

}
