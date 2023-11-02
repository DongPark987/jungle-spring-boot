package jungle.Domain.Member;

import jakarta.persistence.*;
import jungle.Domain.Post.Post;

import java.util.ArrayList;
import java.util.List;

@Entity // DB 테이블 역할을 합니다.
public class Member {
    // ID가 자동으로 생성 및 증가합니다.
    @Id
    @Column(name = "member_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    // nullable: null 허용 여부
    // unique: 중복 허용 여부 (false 일때 중복 허용)
    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @OneToMany(mappedBy = "member", fetch = FetchType.EAGER)
    private List<Post> orders = new ArrayList<>();

}