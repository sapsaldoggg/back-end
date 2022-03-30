package login.jwtlogin.controller;

import login.jwtlogin.auth.PrincipalDetails;
import login.jwtlogin.controller.memberDTO.MyPageDto;
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
    public MyPageDto detail(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        Member member = principalDetails.getMember();
        MyPageDto myPageDto = MyPageDto.builder()
                .nickname(member.getNickname())
                .sex(member.getSex())
                .university(member.getUniversity())
                .dept(member.getDept())
                .sno(member.getSno())
                .build();
        return myPageDto;
    }



}
