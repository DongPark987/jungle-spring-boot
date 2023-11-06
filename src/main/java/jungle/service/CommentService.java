package jungle.service;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jungle.domain.Comment.Comment;
import jungle.domain.Comment.Dto.CommentDeleteDto;
import jungle.domain.Comment.Dto.CommentRequestDto;
import jungle.domain.Comment.Dto.CommentResponseDto;
import jungle.domain.Member.Member;
import jungle.domain.Member.MemberRoleEnum;
import jungle.domain.Post.Post;
import jungle.exception.ErrorCode.UserErrorCode;
import jungle.exception.RestApiException;
import jungle.repository.CommentRepository;
import jungle.repository.MemberRepository;
import jungle.repository.PostRepository;
import jungle.security.Jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
            throw new RestApiException(UserErrorCode.NOT_EXIST_POST);
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
                throw new RestApiException(UserErrorCode.INVALID_TOKEN);
            }

        } catch (Exception e) {
            log.error(e.toString());
            throw new RestApiException(UserErrorCode.INVALID_ACCESS);
        }

        if (member.isPresent()) {
            Comment comment = new Comment(form.getContent(), post, member.get());
            commentRepository.create(comment);
            return new CommentResponseDto(comment.getCommentId(), post.getPost_id()
                    , member.get().getId(), comment.getContent(),comment.getLikeCnt(),comment.getDislike(), comment.getCreatedAt(), comment.getModifiedAt());
        }
        return null;
    }

    public CommentResponseDto update(CommentRequestDto form, HttpServletRequest request) {

        Comment comment = commentRepository.findOne(form.getComment_id());
        if (comment == null) {
            throw new RestApiException(UserErrorCode.NOT_EXIST_COMMENT);
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

        if (comment.getMember() == member.get() || member.get().getRole() == MemberRoleEnum.ADMIN) {
            comment.setContent(form.getContent());
            log.info(comment.getCommentId() + "업데이트 완료");
            commentRepository.create(comment);
            return new CommentResponseDto(comment.getCommentId(), comment.getPost().getPost_id()
                    ,comment.getMember().getId(), comment.getContent(),comment.getLikeCnt(),comment.getDislike(),comment.getCreatedAt(), comment.getModifiedAt());
        } else {
            throw new RestApiException(UserErrorCode.INVALID_ACCESS);
        }
    }
    public void delete(CommentDeleteDto commentDeleteDto, HttpServletRequest request) {

        String jwtToken = jwtUtil.resolveToken(request);
        if (jwtUtil.validateToken(jwtToken)) {
            Claims claims = jwtUtil.getUserInfoFromToken(jwtToken);
            log.info(claims.getSubject());
            Comment comment = commentRepository.findOne(commentDeleteDto.getComment_id());
            Member member = memberService.findMemberByName(claims.getSubject());
            if (comment.getMember() == member || member.getRole() == MemberRoleEnum.ADMIN) {
                commentRepository.delete(comment);
            }

        } else {
            throw new RestApiException(UserErrorCode.INVALID_TOKEN);
        }
    }
    public List<CommentResponseDto> findAllByPostId(Long postId) {
        return commentRepository.findByPostId(postId);
    }

}
