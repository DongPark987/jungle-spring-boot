package jungle.Domain.Post;


import jakarta.persistence.*;
import jungle.Domain.Member.Member;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter @Setter
public class Post {
    // ID가 자동으로 생성 및 증가합니다.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @Column(nullable = false, unique = true)
    String name;

//    @Column(nullable = false, unique = true)
    String pw;

    String title;

    String content;

    private LocalDateTime postDate;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    //메서드 구현



}
