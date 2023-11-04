package jungle.service;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jungle.domain.Member.Member;
import jungle.domain.Post.Dto.PostDeleteDto;
import jungle.domain.Post.Dto.PostDto;
import jungle.domain.Post.Dto.PostResponseDto;
import jungle.domain.Post.Post;
import jungle.security.Jwt.JwtUtil;
import jungle.exception.AuthenticationException;
import jungle.repository.MemberRepository;
import jungle.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@Slf4j
//@Transactional(readOnly = true)
@Transactional
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final MemberService memberService;
    private final JwtUtil jwtUtil;

    public PostResponseDto create(PostDto form, HttpServletRequest request) {

        Optional<Member> member;
        String jwtToken = jwtUtil.resolveToken(request);

        try {
            if (jwtUtil.validateToken(jwtToken)) {
                Claims claims = jwtUtil.getUserInfoFromToken(jwtToken);
                member = memberRepository.findByUsername(claims.getSubject());
                log.info(claims.getSubject());
            } else {
                throw new IllegalAccessException("접근 권한 없음");
            }

        } catch (Exception e) {
            log.error(e.toString());
            throw new AuthenticationException("게시글 작성 권한 없음");
        }

        if (member.isPresent()) {
            Post post = new Post(form.getTitle(), form.getContent(), member.get());
            post.setTitle(form.getTitle());
            post.setContent(form.getContent());
            postRepository.create(post);
            return new PostResponseDto(post.getId(), post.getMember().getUsername(), post.getTitle(), post.getContent(),
                    post.getCreatedAt(), post.getModifiedAt());
        }
        return null;
    }

    public PostResponseDto update(PostDto form, HttpServletRequest request) {

        try {
            Post post = postRepository.findOne(form.getId());

            if (post == null) {
                throw new AuthenticationException("게시글 없음");
            }

            Optional<Member> member;
            String jwtToken = jwtUtil.resolveToken(request);
            if (jwtUtil.validateToken(jwtToken)) {
                Claims claims = jwtUtil.getUserInfoFromToken(jwtToken);
                member = memberRepository.findByUsername(claims.getSubject());
                log.info(claims.getSubject());
            } else {
                throw new IllegalAccessException("접근 권한 없음");
            }

            if (post.getMember() == member.get()) {
                post.setTitle(form.getTitle());
                post.setContent(form.getContent());
                log.info(post.getId() + "업데이트 완료");
                postRepository.create(post);
                return new PostResponseDto(post.getId(), post.getMember().getUsername(), post.getTitle(), post.getContent(),
                        post.getCreatedAt(), post.getModifiedAt());
            } else {
                throw new IllegalAccessException("접근 권한 없음");
            }

        } catch (Exception e) {
            log.error(e.toString());
            throw new AuthenticationException("게시글 변경 권한 없음");
        }


    }

    public void delete(PostDeleteDto postDeleteDto, HttpServletRequest request) throws IllegalAccessException {

        String jwtToken = jwtUtil.resolveToken(request);
        if (jwtUtil.validateToken(jwtToken)) {
            Claims claims = jwtUtil.getUserInfoFromToken(jwtToken);
            log.info(claims.getSubject());
            Post post = postRepository.findOne(postDeleteDto.getId());
            Member member = memberService.findMemberByName(claims.getSubject());
            if (post.getMember() == member) {
                postRepository.delete(post);
            }

        } else {
            throw new IllegalAccessException("접근 권한 없음");
        }
    }

    public List<Post> findAll() {
        return postRepository.findAll();
    }

    public List<PostDto> findAllOrdered() {
        List<Post> posts =  postRepository.findAllOrdered();
        List<PostDto> postDtoList = new ArrayList<>();
        for(Post i : posts){
            PostDto postDto = new PostDto(i.getId(),i.getTitle(),i.getContent());
            postDtoList.add(postDto);
        }
        return postDtoList;
    }

    public Post findById(Long id) {
        return postRepository.findOne(id);
    }


}
