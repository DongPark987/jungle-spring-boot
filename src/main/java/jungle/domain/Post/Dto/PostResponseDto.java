package jungle.domain.Post.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
@AllArgsConstructor
public class PostResponseDto {

    private Long post_id;
    private String name;
    private String title;
    private String content;


    private long like_cnt;
    private long dislike_cnt;

    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

}
