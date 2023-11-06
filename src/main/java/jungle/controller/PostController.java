package jungle.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jungle.domain.Comment.Dto.CommentResponseDto;
import jungle.domain.CommonDto.CommonResponseDto;
import jungle.domain.Post.Dto.PostAndCommentRequestDto;
import jungle.domain.Post.Dto.PostDeleteDto;
import jungle.domain.Post.Dto.PostResponseDto;
import jungle.domain.Post.Dto.PostRequestDto;
import jungle.exception.ErrorCode.UserErrorCode;
import jungle.exception.RestApiException;
import jungle.security.Jwt.JwtUtil;
import jungle.service.CommentService;
import jungle.service.MemberService;
import jungle.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
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
            throw new RestApiException(UserErrorCode.INVALID_REQUEST);
        }

        return postService.create(form, request);
    }

    //게시글 수정
    @PutMapping()
    public PostResponseDto update(@Valid @RequestBody PostRequestDto form, BindingResult result, HttpServletRequest request){
        if (result.hasErrors()) {
            throw new RestApiException(UserErrorCode.INVALID_REQUEST);
        }
        return postService.update(form,request);
    }

    @PostMapping("/delete")
    public ResponseEntity<CommonResponseDto> delete(@Valid @RequestBody PostDeleteDto postDeleteDto, BindingResult result, HttpServletRequest request){
        if (result.hasErrors()) {
            throw new RestApiException(UserErrorCode.INVALID_REQUEST);
        }
        postService.delete(postDeleteDto,request);
        return ResponseEntity.ok(new CommonResponseDto("게시글 삭제 성공", 200));
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @GetMapping()
    public List<PostAndCommentRequestDto> lookup(@Valid PostRequestDto form, BindingResult result) {
        List<PostAndCommentRequestDto> postAndCommentRequestDtoList = new ArrayList<>();
        List<PostResponseDto> postResponseDtoList = postService.findAllOrdered();
        for(PostResponseDto i: postResponseDtoList){
            List<CommentResponseDto> commentResponseDtoList = commentService.findAllByPostId(i.getPost_id());
            postAndCommentRequestDtoList.add(new PostAndCommentRequestDto(i.getPost_id(),i.getName(),i.getTitle(),i.getContent(),i.getLike_cnt(),i.getDislike_cnt(),commentResponseDtoList));
        }
        return postAndCommentRequestDtoList;
    }

    @GetMapping("/{postId}")
    public PostAndCommentRequestDto lookupById(@PathVariable("postId") Long postId) {
        PostResponseDto post= postService.findById(postId);
        List<CommentResponseDto> commentList = commentService.findAllByPostId(postId);

        return new PostAndCommentRequestDto(post.getPost_id(),post.getName(),post.getTitle(),post.getContent(),post.getLike_cnt(),post.getDislike_cnt(),commentList);

    }

}
