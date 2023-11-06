package jungle.service;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jungle.domain.Member.Member;
import jungle.domain.Member.MemberRoleEnum;
import jungle.domain.Post.Dto.PostDeleteDto;
import jungle.domain.Post.Dto.PostRequestDto;
import jungle.domain.Post.Dto.PostResponseDto;
import jungle.domain.Post.Post;
import jungle.exception.ErrorCode.UserErrorCode;
import jungle.exception.RestApiException;
import jungle.security.Jwt.JwtUtil;
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

    public PostResponseDto create(PostRequestDto form, HttpServletRequest request) {

        Optional<Member> member;
        String jwtToken = jwtUtil.resolveToken(request);

        if (jwtUtil.validateToken(jwtToken)) {
            Claims claims = jwtUtil.getUserInfoFromToken(jwtToken);
            member = memberRepository.findByUsername(claims.getSubject());
            log.info(claims.getSubject());
        } else {
            throw new RestApiException(UserErrorCode.INVALID_TOKEN);
        }

        if (member.isPresent()) {
            Post post = new Post(form.getTitle(), form.getContent(), member.get());
            post.setTitle(form.getTitle());
            post.setContent(form.getContent());
            postRepository.create(post);
            return new PostResponseDto(post.getPost_id(), post.getMember().getUsername(), post.getTitle(), post.getContent(),
                    post.getLikeCnt(),post.getDislikeCnt(),post.getCreatedAt(), post.getModifiedAt());
        }
        return null;
    }

    public PostResponseDto update(PostRequestDto form, HttpServletRequest request) {


        Post post = postRepository.findOne(form.getId());

        if (post == null) {
            throw new RestApiException(UserErrorCode.NOT_EXIST_POST);

        }

        Optional<Member> member;
        String jwtToken = jwtUtil.resolveToken(request);
        if (jwtUtil.validateToken(jwtToken)) {
            Claims claims = jwtUtil.getUserInfoFromToken(jwtToken);
            member = memberRepository.findByUsername(claims.getSubject());
            log.info(claims.getSubject());
        } else {
            throw new RestApiException(UserErrorCode.INVALID_TOKEN);
        }

        if (post.getMember() == member.get() || member.get().getRole() == MemberRoleEnum.ADMIN) {
            post.setTitle(form.getTitle());
            post.setContent(form.getContent());
            log.info(post.getPost_id() + "업데이트 완료");
            postRepository.create(post);
            return new PostResponseDto(post.getPost_id(), post.getMember().getUsername(), post.getTitle(), post.getContent(),
                    post.getLikeCnt(),post.getDislikeCnt(),post.getCreatedAt(), post.getModifiedAt());
        } else {
            throw new RestApiException(UserErrorCode.INVALID_ACCESS);
        }


    }

    public void delete(PostDeleteDto postDeleteDto, HttpServletRequest request) {

        String jwtToken = jwtUtil.resolveToken(request);

        if (jwtUtil.validateToken(jwtToken)) {
            Claims claims = jwtUtil.getUserInfoFromToken(jwtToken);
            log.info(claims.getSubject());
            Post post = postRepository.findOne(postDeleteDto.getId());
            Member member = memberService.findMemberByName(claims.getSubject());
            if (post.getMember() == member || member.getRole() == MemberRoleEnum.ADMIN) {
                postRepository.delete(post);
            } else {
                throw new RestApiException(UserErrorCode.INVALID_ACCESS);
            }

        } else {
            throw new RestApiException(UserErrorCode.INVALID_TOKEN);
        }
    }

    public List<Post> findAll() {
        return postRepository.findAll();
    }

    public List<PostResponseDto> findAllOrdered() {
        List<Post> posts = postRepository.findAllOrdered();
        List<PostResponseDto> postResponseDtoList = new ArrayList<>();
        for (Post i : posts) {
            PostResponseDto postResponseDto = new PostResponseDto(i.getPost_id(), i.getMember().getUsername(), i.getTitle(), i.getContent(),i.getLikeCnt(),i.getDislikeCnt() ,i.getCreatedAt(), i.getModifiedAt());
            postResponseDtoList.add(postResponseDto);
        }
        return postResponseDtoList;
    }

    public PostResponseDto findById(Long id) {

        Post post = postRepository.findOne(id);
        return new PostResponseDto(post.getPost_id(), post.getMember().getUsername(), post.getTitle(), post.getContent(),post.getLikeCnt() ,post.getDislikeCnt(),post.getCreatedAt(), post.getModifiedAt());
    }


}
