package jungle.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jungle.domain.Post.Dto.PostDeleteDto;
import jungle.domain.Post.Dto.PostResponseDto;
import jungle.domain.Post.Post;
import jungle.domain.Post.Dto.PostDto;
import jungle.security.Jwt.JwtUtil;
import jungle.exception.AuthenticationException;
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
@RequestMapping("/post")
public class PostController {
    private final PostService postService;
    private final MemberService memberService;
    private final JwtUtil jwtUtil;

    //게시글 작성
    @PostMapping()
    public PostResponseDto create(@RequestHeader Map<String, String> headers, @Valid @RequestBody PostDto form, BindingResult result, HttpServletRequest request) {
        if (result.hasErrors()) {
            System.out.println(result.getAllErrors());
        }

        return postService.create(form, request);
    }

    //게시글 수정
    @PutMapping()
    public PostResponseDto update(@Valid @RequestBody PostDto form, BindingResult result, HttpServletRequest request) throws AuthenticationException {
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
    public List<PostDto> lookup(@Valid PostDto form, BindingResult result) {
        return postService.findAllOrdered();
    }

    @GetMapping("/{postId}")
    public Post lookupById(@PathVariable("postId") Long postId) {
        return postService.findById(postId);
    }

}
