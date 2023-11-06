package jungle.service;

import jakarta.servlet.http.HttpServletResponse;
import jungle.domain.Member.Dto.LoginRequestDto;
import jungle.domain.Member.Dto.SignupRequestDto;
import jungle.domain.Member.Member;
import jungle.domain.Member.MemberRoleEnum;
import jungle.exception.ErrorCode.UserErrorCode;
import jungle.exception.RestApiException;
import jungle.security.Jwt.JwtUtil;
import jungle.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository userRepository;
    private final JwtUtil jwtUtil;
    private static final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";

    @Transactional
    public void signup(SignupRequestDto signupRequestDto) {
        String username = signupRequestDto.getUsername();
        String password = signupRequestDto.getPassword();
        String email = signupRequestDto.getEmail();

        // 회원 중복 확인
        Optional<Member> found = userRepository.findByUsername(username);
        if (found.isPresent()) {
            throw new RestApiException(UserErrorCode.DUPLICATE_USER);
        }

        // 사용자 ROLE 확인
        MemberRoleEnum role = MemberRoleEnum.USER;
        if (signupRequestDto.isAdmin()) {
            if (!signupRequestDto.getAdminToken().equals(ADMIN_TOKEN)) {
                throw new IllegalArgumentException("관리자 암호가 틀려 등록이 불가능합니다.");
            }
            role = MemberRoleEnum.ADMIN;
        }

        Member member = new Member(username, password, email, role);
        userRepository.save(member);
    }

    @Transactional(readOnly = true)
    public void login(LoginRequestDto loginRequestDto, HttpServletResponse response) {
        String username = loginRequestDto.getUsername();
        String password = loginRequestDto.getPassword();
        // 사용자 확인
        Member member = userRepository.findByUsername(username).orElseThrow(
                () -> new RestApiException(UserErrorCode.INVALID_USERNAME));
        // 비밀번호 확인
        if (!member.getPassword().equals(password)) {
            throw new RestApiException(UserErrorCode.INVALID_PASSWORD);
        }
        //토큰 발급
        String token = jwtUtil.createToken(member.getUsername(), member.getRole());
        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, token);
        log.info("발급토큰: " + token);
    }

    public Member findMemberByName(String username) {
        return userRepository.findByUsername(username).orElseThrow(
                () -> new RestApiException(UserErrorCode.INACTIVE_USER)
        );
    }
}