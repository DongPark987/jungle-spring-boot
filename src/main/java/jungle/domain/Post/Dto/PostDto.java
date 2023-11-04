package jungle.domain.Post.Dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class PostDto {

    private  Long id;
    @NotEmpty(message = "게시글 제목은 필수 입니다.")
    private String title;
    @NotEmpty(message = "게시글 내용은 필수 입니다.")
    private String content;

    public PostDto(Long id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }
}
