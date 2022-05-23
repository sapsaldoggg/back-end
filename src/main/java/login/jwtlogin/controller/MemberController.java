package login.jwtlogin.controller;

import login.jwtlogin.auth.PrincipalDetails;
import login.jwtlogin.controller.memberDto.MyPageDto;
import login.jwtlogin.domain.Member;
import login.jwtlogin.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;

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
                .reliability(member.getReliability())
                .build();

        return myPageDto;
    }

    //다른 회원들 정보 조회 ( ex) 파티멤버 클릭시 해당 멤버 정보 확인 )
    @GetMapping("/{member_id}")
    public MyPageDto memberInfo(@PathVariable(name = "member_id") Long id) {
        Member member = memberRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("존재하지 않는 회원입니다")
        );
        MyPageDto myPageDto = MyPageDto.builder()
                .nickname(member.getNickname())
                .sex(member.getSex())
                .university(member.getUniversity())
                .dept(member.getDept())
                .sno(member.getSno())
                .reliability(member.getReliability())
                .build();
        return myPageDto;
    }


    @GetMapping("/test")
    public void test(@Header(name = "Authorization") String token) {


    }



}
