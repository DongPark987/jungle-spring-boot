package jungle.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jungle.domain.Member.Dto.LoginRequestDto;
import jungle.domain.Member.Dto.SignupRequestDto;
import jungle.security.Jwt.JwtUtil;
import jungle.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

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
    public String signup(@Valid @RequestBody SignupRequestDto signupRequestDto, BindingResult result) {
        if (result.hasErrors()) {
            System.out.println(result.getAllErrors());
            return "Fail";
        }
        try {
            memberService.signup(signupRequestDto);
        }catch (Exception e) {
            log.error(e.toString());
            return "Fail";
        }
        return "success";
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginRequestDto loginRequestDto,  BindingResult result, HttpServletResponse response) {
        if (result.hasErrors()) {
            System.out.println(result.getAllErrors());
            return "Fail";
        }
        try {
            memberService.login(loginRequestDto,response);
        }catch (Exception e) {
            log.error(e.toString());
            return "Fail";
        }
        return "success";
    }


}
