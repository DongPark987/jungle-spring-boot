package jungle.controller;

import jakarta.validation.Valid;
import jungle.Domain.Post.Post;
import jungle.Domain.Post.Dto.PostForm;
import jungle.exception.AuthenticationException;
import jungle.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/post")
public class PostController {
    private final PostService postService;

    @PostMapping()
    public Post create(@RequestHeader Map<String,String> headers, @Valid @RequestBody PostForm form, BindingResult result) {
        if (result.hasErrors()) {
            System.out.println(result.getAllErrors());
        }
        log.info(headers.get("access_token"));
        Post post = new Post();
        post.setName(form.getName());
        post.setPw(form.getPw());
        post.setTitle(form.getTitle());
        post.setContent(form.getContent());
        post.setPostDate(LocalDateTime.now());
        postService.create(post);

        return post;
    }

    @PutMapping()
    public Post update(@Valid @RequestBody PostForm form, BindingResult result) throws AuthenticationException {
        if (result.hasErrors()) {
            System.out.println(result.getAllErrors());
        }
        Post post = postService.findById(form.getId());
        if (post != null && post.getPw().equals(form.getPw())) {
            post.setName(form.getName());
            post.setTitle(form.getTitle());
            post.setContent(form.getContent());
            post.setPostDate(LocalDateTime.now());
            postService.create(post);
        } else {
            throw new AuthenticationException("인증 실패");
        }
        return post;

    }

    @PostMapping("/delete")
    public String delete(@Valid @RequestBody PostForm form, BindingResult result) throws AuthenticationException {
        if (result.hasErrors()) {
            System.out.println(result.getAllErrors());
        }
        Post post = postService.findById(form.getId());
        if (post != null && post.getPw().equals(form.getPw())) {
            postService.delete(post);
        } else {
            throw new AuthenticationException("인증 실패");
        }
        return "success";

    }



    @GetMapping()
    public List<Post> lookup(@Valid PostForm form, BindingResult result) {
        return postService.findAllOrdered();
    }

    @GetMapping("/{postId}")
    public Post lookupById(@PathVariable("postId") Long postId) {
        return postService.findById(postId);
    }

}
