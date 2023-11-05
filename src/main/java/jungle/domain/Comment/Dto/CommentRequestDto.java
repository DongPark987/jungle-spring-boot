package jungle.domain.Comment.Dto;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jungle.domain.Member.Member;
import jungle.domain.Post.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommentRequestDto {

    private long comment_id;
    private String content;
    @NotNull(message = "포스트 아이디는 필수입니다.")
    private long post_id;

}
