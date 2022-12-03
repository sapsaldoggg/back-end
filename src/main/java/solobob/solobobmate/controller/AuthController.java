package solobob.solobobmate.controller;

import org.springframework.http.ResponseCookie;
import solobob.solobobmate.auth.jwt.TokenDto;
import solobob.solobobmate.controller.exception.ErrorCode;
import solobob.solobobmate.controller.exception.SoloBobException;
import solobob.solobobmate.controller.memberDto.LoginDto;
import solobob.solobobmate.controller.memberDto.MemberInfoDto;
import solobob.solobobmate.domain.Member;
import solobob.solobobmate.repository.MemberRepository;
import solobob.solobobmate.service.AuthService;
import solobob.solobobmate.service.email.VerifyCodeService;
import solobob.solobobmate.controller.memberDto.JoinDto;
import solobob.solobobmate.domain.email.VerifyCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;
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
    public ResponseEntity login(@RequestBody @Validated LoginDto loginDto, HttpServletResponse response) {

        TokenDto tokenDto = authService.login(loginDto);

        response.setHeader("Authorization", "Bearer "+tokenDto.getAccessToken());
        response.setHeader("Set-Cookie", setRefreshToken(tokenDto.getRefreshToken()).toString());

        Member member = memberRepository.findByLoginId(loginDto.getLoginId()).orElseThrow(
                () -> new SoloBobException(ErrorCode.NOT_FOUND_MEMBER)
        );

        return ResponseEntity.ok(new MemberInfoDto(member.getId(), member.getNickname()));
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
    public ResponseEntity duplicateId(@RequestBody Map<String, String> loginIdMap) {
        log.info(loginIdMap.get("loginId"));
        if (memberRepository.findByLoginId(loginIdMap.get("loginId")).isPresent()) {
            return ResponseEntity.ok(false);
        } else {
            return ResponseEntity.ok(true);
        }
    }


    //닉네임 중복 검사
    @PostMapping("/duplicate-nickname")
    public ResponseEntity duplicateNickname(@RequestBody Map<String, String> nicknameMap) {
        log.info(nicknameMap.get("nickname"));
        if (memberRepository.findByNickname(nicknameMap.get("nickname")).isPresent()) {
            return ResponseEntity.ok(false);
        } else {
            return ResponseEntity.ok(true);
        }
    }

    //메일로 인증코드 보내기
    @PostMapping("/mail-auth")
    public ResponseEntity mailAuthReq(@RequestBody Map<String, String> emailMap) {
        log.info(emailMap.get("email"));
        verifyCodeService.createVerifyCode(emailMap.get("email"));
        return ResponseEntity.ok(true);
    }


    //메일 인증 결과
    @PostMapping("/mailcode-auth")
    public ResponseEntity mailCodeAuth(@RequestBody Map<String, String> codeMap) {
        log.info(codeMap.get("code"));
        Optional<VerifyCode> result = authService.confirmEmail(codeMap.get("code"));
        if (result.isEmpty()) {
            return ResponseEntity.ok(false);
        }
        return ResponseEntity.ok(true);
    }
}
