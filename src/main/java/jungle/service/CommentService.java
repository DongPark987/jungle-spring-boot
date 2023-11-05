package jungle.service;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jungle.domain.Comment.Comment;
import jungle.domain.Comment.Dto.CommentDeleteDto;
import jungle.domain.Comment.Dto.CommentRequestDto;
import jungle.domain.Comment.Dto.CommentResponseDto;
import jungle.domain.Member.Member;
import jungle.domain.Post.Dto.PostDeleteDto;
import jungle.domain.Post.Dto.PostResponseDto;
import jungle.domain.Post.Post;
import jungle.exception.AuthenticationException;
import jungle.repository.CommentRepository;
import jungle.repository.MemberRepository;
import jungle.repository.PostRepository;
import jungle.security.Jwt.JwtUtil;
import lombok.Builder;
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
public class CommentService {
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final MemberService memberService;
    private final CommentRepository commentRepository;
    private final JwtUtil jwtUtil;

    public CommentResponseDto create(CommentRequestDto form, HttpServletRequest request) {

        Optional<Member> member;
        Post post = postRepository.findOne(form.getPost_id());
        if (post == null) {
            throw new AuthenticationException("게시글 없음");
        }
        //토큰 추출
        String jwtToken = jwtUtil.resolveToken(request);

        try {
            //토큰 검증
            if (jwtUtil.validateToken(jwtToken)) {
                Claims claims = jwtUtil.getUserInfoFromToken(jwtToken);
                member = memberRepository.findByUsername(claims.getSubject());
                log.info(claims.getSubject());
            } else {
                throw new IllegalAccessException("접근 권한 없음");
            }

        } catch (Exception e) {
            log.error(e.toString());
            throw new AuthenticationException("댓글 작성 권한 없음");
        }

        if (member.isPresent()) {
            Comment comment = new Comment(form.getContent(), post, member.get());

            commentRepository.create(comment);
            return new CommentResponseDto(comment.getCommentId(), post.getPost_id()
                    , member.get().getId(), comment.getContent(), comment.getCreatedAt(), comment.getModifiedAt());
        }
        return null;
    }

    public CommentResponseDto update(CommentRequestDto form, HttpServletRequest request) {

        try {
            Comment comment = commentRepository.findOne(form.getComment_id());

            if (comment == null) {
                throw new AuthenticationException("코멘트 없음");
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

            if (comment.getMember() == member.get()) {
                comment.setContent(form.getContent());
                log.info(comment.getCommentId() + "업데이트 완료");
                commentRepository.create(comment);
                return new CommentResponseDto(comment.getCommentId(), comment.getPost().getPost_id()
                        , member.get().getId(), comment.getContent(), comment.getCreatedAt(), comment.getModifiedAt());
            } else {
                throw new IllegalAccessException("접근 권한 없음");
            }

        } catch (Exception e) {
            log.error(e.toString());
            throw new AuthenticationException("댓글 변경 권한 없음");
        }

    }

    public void delete(CommentDeleteDto commentDeleteDto, HttpServletRequest request) throws IllegalAccessException {

        String jwtToken = jwtUtil.resolveToken(request);
        if (jwtUtil.validateToken(jwtToken)) {
            Claims claims = jwtUtil.getUserInfoFromToken(jwtToken);
            log.info(claims.getSubject());
            Comment comment = commentRepository.findOne(commentDeleteDto.getComment_id());
            Member member = memberService.findMemberByName(claims.getSubject());
            if (comment.getMember() == member) {
                commentRepository.delete(comment);
            }

        } else {
            throw new IllegalAccessException("접근 권한 없음");
        }
    }

    public List<CommentResponseDto> findAllByPostId(Long postId) {
        return commentRepository.findByPostId(postId);
    }

}
