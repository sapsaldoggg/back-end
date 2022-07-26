package login.solobobmate.controller;

import login.solobobmate.auth.PrincipalDetails;
import login.solobobmate.controller.exception.ExceptionMessages;
import login.solobobmate.controller.memberDto.MyPageDto;
import login.solobobmate.domain.Member;
import login.solobobmate.repository.MemberRepository;
import login.solobobmate.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityNotFoundException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class MemberController {

    private final MemberRepository memberRepository;
    private final MemberService memberService;

    @GetMapping
    public ResponseEntity myPage(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        Member member = principalDetails.getMember();
        Member findMember = memberRepository.findById(member.getId()).orElseThrow(
                () -> new EntityNotFoundException(ExceptionMessages.NOT_FOUND_MEMBER)
        );
        MyPageDto my = memberService.detail(findMember);
        return ResponseEntity.ok(my);
    }
}
