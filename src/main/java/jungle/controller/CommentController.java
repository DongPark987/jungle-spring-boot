package jungle.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jungle.domain.Comment.Dto.CommentDeleteDto;
import jungle.domain.Comment.Dto.CommentRequestDto;
import jungle.domain.Comment.Dto.CommentResponseDto;
import jungle.domain.Post.Dto.PostDeleteDto;
import jungle.domain.Post.Dto.PostResponseDto;
import jungle.domain.Post.Post;
import jungle.exception.AuthenticationException;
import jungle.security.Jwt.JwtUtil;
import jungle.service.CommentService;
import jungle.service.MemberService;
import jungle.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/comment")
public class CommentController {
    
    private final CommentService commentService; 
    @PostMapping()
    public CommentResponseDto create(@RequestHeader Map<String, String> headers, @Valid @RequestBody CommentRequestDto form, BindingResult result, HttpServletRequest request) {
        if (result.hasErrors()) {
            System.out.println(result.getAllErrors());
        }
        return commentService.create(form, request);
    }
    //게시글 수정
    @PutMapping()
    public CommentResponseDto update(@Valid @RequestBody CommentRequestDto form, BindingResult result, HttpServletRequest request) throws AuthenticationException {
        if (result.hasErrors()) {
            System.out.println(result.getAllErrors());
        }
        return commentService.update(form,request);
    }

    @PostMapping("/delete")
    public String delete(@Valid @RequestBody CommentDeleteDto commentDeleteDto, BindingResult result, HttpServletRequest request) throws AuthenticationException {
        if (result.hasErrors()) {
            System.out.println(result.getAllErrors());
        }
        try {
            commentService.delete(commentDeleteDto,request);

        } catch (Exception e) {
            log.error(e.toString());
            throw new AuthenticationException("코멘트 삭제 권한 없음");
        }

        return "success";
    }

}