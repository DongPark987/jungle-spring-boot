package jungle.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jungle.domain.CommonDto.CommonResponseDto;
import jungle.domain.Member.Dto.LoginRequestDto;
import jungle.domain.Member.Dto.SignupRequestDto;
import jungle.exception.RestApiException;
import jungle.exception.ErrorCode.UserErrorCode;
import jungle.security.Jwt.JwtUtil;
import jungle.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@Slf4j
@RequiredArgsConstructor
@RestController
//@RequestMapping("/api")
public class MemberController {

    private final MemberService memberService;
    private final JwtUtil jwtUtil;

//    @GetMapping("/signup")
//    public ModelAndView signupPage() {
//        return new ModelAndView("signup");
//    }
//
//    @GetMapping("/login")
//    public ModelAndView loginPage() {
//        return new ModelAndView("login");
//    }

    @PostMapping("/signup")
    public ResponseEntity<CommonResponseDto> signup(@Valid @RequestBody SignupRequestDto signupRequestDto, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(new CommonResponseDto("유효하지 않은 가입정보 입니다.",400));
        }
        memberService.signup(signupRequestDto);
        return ResponseEntity.ok(new CommonResponseDto("회원가입 성공", 200));
    }

    @PostMapping("/login")
    public ResponseEntity<CommonResponseDto> login(@RequestBody LoginRequestDto loginRequestDto, BindingResult result, HttpServletResponse response) {
        if (result.hasErrors()) {
            System.out.println(result.getAllErrors());
            throw new RestApiException(UserErrorCode.INACTIVE_USER);
        }

        memberService.login(loginRequestDto,response);

        return ResponseEntity.ok(new CommonResponseDto("로그인 성공", 200));
    }


}
