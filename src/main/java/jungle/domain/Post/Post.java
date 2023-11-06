package jungle.domain.Post;


import jakarta.persistence.*;
import jungle.domain.Comment.Comment;
import jungle.domain.Member.Member;
import jungle.domain.Timestamped;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor
public class Post extends Timestamped {
    // ID가 자동으로 생성 및 증가합니다.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long post_id;

    @Column(nullable = false)
    private String title;

    private String content;


    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "post")
    private List<Comment> Comments;

    @Column(name = "like_cnt")
    long likeCnt = 0;
    @Column(name = "dislike_cnt")
    long dislikeCnt = 0;

    public Post(String title, String content, Member member) {
        this.title = title;
        this.content = content;
        this.member = member;
    }

}
