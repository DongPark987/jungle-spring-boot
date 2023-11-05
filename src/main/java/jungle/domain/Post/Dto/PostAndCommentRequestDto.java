package jungle.domain.Post.Dto;

import jakarta.validation.constraints.NotEmpty;
import jungle.domain.Comment.Dto.CommentRequestDto;
import jungle.domain.Comment.Dto.CommentResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostAndCommentRequestDto {

    private  Long post_id;
    private String post_name;
    private String title;
    private String content;

    List<CommentResponseDto> comment_list = new ArrayList<>();


}
