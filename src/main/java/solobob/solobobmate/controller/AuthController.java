package solobob.solobobmate.controller;

import org.springframework.http.ResponseCookie;
import solobob.solobobmate.auth.jwt.TokenDto;
import solobob.solobobmate.controller.memberDto.LoginDto;
import solobob.solobobmate.service.AuthService;
import solobob.solobobmate.service.email.VerifyCodeService;
import solobob.solobobmate.controller.memberDto.JoinDto;
import solobob.solobobmate.domain.email.VerifyCode;
import solobob.solobobmate.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@Slf4j
public class AuthController {

    private final MemberRepository memberRepository;
    private final VerifyCodeService verifyCodeService;
    private final AuthService authService;

    //회원가입
    @PostMapping("/join")
    public ResponseEntity join(@RequestBody @Validated JoinDto joinDto) {

        authService.save(joinDto);

        return ResponseEntity.ok(true);
    }

    /**
     * 로그인 성공 시, header(Authorization) : accessToken, Cookie : refreshToken 응답
     */
    @PostMapping("/login")
    public void login(@RequestBody @Validated LoginDto loginDto, HttpServletResponse response) {

        TokenDto tokenDto = authService.login(loginDto);

        response.setHeader("Authorization", "Bearer "+tokenDto.getAccessToken());
        response.setHeader("Set-Cookie", setRefreshToken(tokenDto.getRefreshToken()).toString());
    }

    public ResponseCookie setRefreshToken(String refreshToken) {
        ResponseCookie cookie = ResponseCookie.from("RefreshToken", refreshToken)
                .httpOnly(true)
                .secure(true)
                .maxAge(60 * 60 * 24 * 7)  //7일 -> 유효기간
                .sameSite("None")
                .path("/")
                .build();
        return cookie;
    }

    /**
     * 토큰 재발급
     */
    @PostMapping("/refresh")
    public void refresh(@CookieValue(name = "RefreshToken") String refreshToken, HttpServletResponse response) {

        if (refreshToken == null) {

        }
        TokenDto tokenDto = authService.refresh(refreshToken);

        response.setHeader("Authorization", "Bearer "+tokenDto.getAccessToken());
        response.setHeader("Set-Cookie", setRefreshToken(tokenDto.getRefreshToken()).toString());
    }

    //아이디 중복검사
    @PostMapping("/duplicate-loginId")
    public ResponseEntity duplicateId(@RequestBody String loginId) {
        log.info(loginId);
        if (memberRepository.findByLoginId(loginId).isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(false);
        } else {
            return ResponseEntity.ok(true);
        }
    }


    //닉네임 중복 검사
    @PostMapping("/duplicate-nickname")
    public ResponseEntity duplicateNickName(@RequestBody String nickname) {
        log.info(nickname);
        log.info(nickname.getClass().getName());
        if (memberRepository.findByNickName(nickname).isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(false);
        } else {
            return ResponseEntity.ok(true);
        }
    }

    //메일로 인증코드 보내기
    @PostMapping("/mail-auth")
    public ResponseEntity mailAuthReq(@RequestBody String email) {
        verifyCodeService.createVerifyCode(email);
        return ResponseEntity.ok(true);
    }


    //메일 인증 결과
    @PostMapping("/mailcode-auth")
    public ResponseEntity mailCodeAuth(@RequestBody String code) {
        log.info(code);
        Optional<VerifyCode> result = authService.confirmEmail(code);
        if (result.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(false);
        }
        return ResponseEntity.ok(true);
    }
}
