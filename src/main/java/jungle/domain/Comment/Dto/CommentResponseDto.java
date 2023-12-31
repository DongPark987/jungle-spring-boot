package jungle.domain.Comment.Dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentResponseDto {

    private Long comment_id;
    private Long post_id;
    private Long member_id;
    private String content;

    private long like_cnt;
    private long dislike_cnt;

    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

}
