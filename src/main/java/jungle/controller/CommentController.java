package jungle.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jungle.domain.Comment.Dto.CommentDeleteDto;
import jungle.domain.Comment.Dto.CommentRequestDto;
import jungle.domain.Comment.Dto.CommentResponseDto;
import jungle.domain.CommonDto.CommonResponseDto;
import jungle.exception.ErrorCode.UserErrorCode;
import jungle.exception.RestApiException;
import jungle.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

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
            throw new RestApiException(UserErrorCode.INVALID_REQUEST);
        }
        return commentService.create(form, request);
    }
    //게시글 수정
    @PutMapping()
    public CommentResponseDto update(@Valid @RequestBody CommentRequestDto form, BindingResult result, HttpServletRequest request){
        if (result.hasErrors()) {
            throw new RestApiException(UserErrorCode.INVALID_REQUEST);
        }
        return commentService.update(form,request);
    }

    @PostMapping("/delete")
    public ResponseEntity<CommonResponseDto> delete(@Valid @RequestBody CommentDeleteDto commentDeleteDto, BindingResult result, HttpServletRequest request){
        if (result.hasErrors()) {
            throw new RestApiException(UserErrorCode.INVALID_REQUEST);
        }
        commentService.delete(commentDeleteDto,request);
        return ResponseEntity.ok(new CommonResponseDto("댓글 삭제 성공", 200));
    }

}