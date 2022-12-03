package solobob.solobobmate.controller;

import org.springframework.http.ResponseCookie;
import org.springframework.web.bind.annotation.*;
import solobob.solobobmate.auth.config.SecurityUtil;
import solobob.solobobmate.controller.exception.ErrorCode;
import solobob.solobobmate.controller.exception.SoloBobException;
import solobob.solobobmate.controller.memberDto.MyPageDto;
import solobob.solobobmate.domain.Member;
import solobob.solobobmate.repository.MemberRepository;
import solobob.solobobmate.service.AuthService;
import solobob.solobobmate.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import solobob.solobobmate.service.PartyService;

import javax.servlet.http.HttpServletResponse;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class MemberController {

    private final AuthService authService;
    private final MemberService memberService;
    private final PartyService partyService;



    @GetMapping
    public ResponseEntity myPage() {
        String loginId = authService.getLoginId();
        MyPageDto my = memberService.detail(loginId);
        return ResponseEntity.ok(my);
    }

    //신뢰도 업
    @PostMapping("/{member_id}/like")
    public void memberLike(@PathVariable(name = "member_id") Long id) {
        String loginId = authService.getLoginId();
        memberService.likeUp(loginId);
    }

    @GetMapping("/myParty")
    public ResponseEntity myParty() {
        String loginId = authService.getLoginId();
        return ResponseEntity.ok(partyService.myPartyInfo(loginId));
    }

    @PostMapping("/logout")
    public void logout(HttpServletResponse response) {

        ResponseCookie cookie = ResponseCookie.from("RefreshToken", null)
                .maxAge(0)
                .path("/")
                .sameSite("None")  //빌드 후 배포 시엔 수정 예정
                .secure(true)
                .httpOnly(true)
                .build();

        response.setHeader("Set-Cookie", cookie.toString());
    }


}
