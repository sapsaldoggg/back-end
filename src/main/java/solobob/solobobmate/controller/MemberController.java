package solobob.solobobmate.controller;

import org.springframework.http.ResponseCookie;
import org.springframework.web.bind.annotation.PostMapping;
import solobob.solobobmate.auth.config.SecurityUtil;
import solobob.solobobmate.controller.exception.ExceptionMessages;
import solobob.solobobmate.controller.memberDto.MyPageDto;
import solobob.solobobmate.domain.Member;
import solobob.solobobmate.repository.MemberRepository;
import solobob.solobobmate.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class MemberController {

    private final MemberRepository memberRepository;
    private final MemberService memberService;

    public Member getMember() {
        Member member = memberRepository.findByLoginId(SecurityUtil.getCurrentMemberId()).orElseThrow(
                () -> new EntityNotFoundException(ExceptionMessages.NOT_FOUND_MEMBER)
        );
        return member;
    }

    @GetMapping
    public ResponseEntity myPage() {
        Member member = getMember();
        MyPageDto my = memberService.detail(member);
        return ResponseEntity.ok(my);
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
