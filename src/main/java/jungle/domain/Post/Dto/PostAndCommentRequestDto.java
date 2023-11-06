package jungle.domain.Post.Dto;

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
    private String member_name;
    private String title;
    private String content;

    private long like_cnt;
    private long dislike_cnt;

    List<CommentResponseDto> comment_list = new ArrayList<>();

}
