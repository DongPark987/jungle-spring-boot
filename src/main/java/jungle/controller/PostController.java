package jungle.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jungle.domain.Comment.Comment;
import jungle.domain.Comment.Dto.CommentResponseDto;
import jungle.domain.Post.Dto.PostAndCommentRequestDto;
import jungle.domain.Post.Dto.PostDeleteDto;
import jungle.domain.Post.Dto.PostResponseDto;
import jungle.domain.Post.Post;
import jungle.domain.Post.Dto.PostRequestDto;
import jungle.security.Jwt.JwtUtil;
import jungle.exception.AuthenticationException;
import jungle.service.CommentService;
import jungle.service.MemberService;
import jungle.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/post")
public class PostController {
    private final PostService postService;
    private final MemberService memberService;
    private final CommentService commentService;
    private final JwtUtil jwtUtil;

    //게시글 작성
    @PostMapping()
    public PostResponseDto create(@RequestHeader Map<String, String> headers, @Valid @RequestBody PostRequestDto form, BindingResult result, HttpServletRequest request) {
        if (result.hasErrors()) {
            System.out.println(result.getAllErrors());
        }

        return postService.create(form, request);
    }

    //게시글 수정
    @PutMapping()
    public PostResponseDto update(@Valid @RequestBody PostRequestDto form, BindingResult result, HttpServletRequest request) throws AuthenticationException {
        if (result.hasErrors()) {
            System.out.println(result.getAllErrors());
        }

        return postService.update(form,request);
    }

    @PostMapping("/delete")
    public String delete(@Valid @RequestBody PostDeleteDto postDeleteDto, BindingResult result, HttpServletRequest request) throws AuthenticationException {
        if (result.hasErrors()) {
            System.out.println(result.getAllErrors());
        }
        try {
            postService.delete(postDeleteDto,request);

        } catch (Exception e) {
            log.error(e.toString());
            throw new AuthenticationException("게시글 삭제 권한 없음");
        }

        return "success";
    }


    @GetMapping("")
    public List<PostAndCommentRequestDto> lookup(@Valid PostRequestDto form, BindingResult result) {
        List<PostAndCommentRequestDto> postAndCommentRequestDtoList = new ArrayList<>();
        List<PostResponseDto> postResponseDtoList = postService.findAllOrdered();
        for(PostResponseDto i: postResponseDtoList){
            List<CommentResponseDto> commentResponseDtoList = commentService.findAllByPostId(i.getId());
            postAndCommentRequestDtoList.add(new PostAndCommentRequestDto(i.getId(),i.getName(),i.getTitle(),i.getContent(),commentResponseDtoList));
        }
        return postAndCommentRequestDtoList;
    }

    @GetMapping("/{postId}")
    public Post lookupById(@PathVariable("postId") Long postId) {
        return postService.findById(postId);
    }

}
