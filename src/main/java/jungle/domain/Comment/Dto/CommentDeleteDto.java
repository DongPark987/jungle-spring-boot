package jungle.domain.Comment.Dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentDeleteDto {

    @NotNull(message = "댓글 아이디는 필수 입니다.")
    private long comment_id;

}
