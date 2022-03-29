package login.jwtlogin.controller;

import login.jwtlogin.auth.PrincipalDetails;
import login.jwtlogin.domain.Member;
import login.jwtlogin.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/user")
public class MemberController {

    private MemberRepository memberRepository;

    //회원 정보 (마이페이지)
    @GetMapping
    public Member detail(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        Member member = principalDetails.getMember();
        return member;
    }



}
