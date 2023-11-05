package jungle.domain.Post.Dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class PostRequestDto {

    private  Long id;
    @NotEmpty(message = "게시글 제목은 필수 입니다.")
    private String title;
    @NotEmpty(message = "게시글 내용은 필수 입니다.")
    private String content;

    public PostRequestDto(Long id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }
}
