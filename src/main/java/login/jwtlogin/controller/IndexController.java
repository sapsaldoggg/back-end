package login.jwtlogin.controller;

import login.jwtlogin.auth.PrincipalDetailService;
import login.jwtlogin.auth.email.VerifyCodeService;
import login.jwtlogin.controller.memberDTO.JoinDto;
import login.jwtlogin.controller.memberDTO.MyPageDto;
import login.jwtlogin.domain.Member;
import login.jwtlogin.domain.email.VerifyCode;
import login.jwtlogin.result.ErrorResult;
import login.jwtlogin.repository.MemberRepository;
import login.jwtlogin.result.Result;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@Slf4j
public class IndexController {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final VerifyCodeService verifyCodeService;
    private final PrincipalDetailService principalDetailService;

    //회원가입
    @PostMapping("/join")
    public Object join(@Validated @RequestBody JoinDto joinDto) {
        log.info(joinDto.getUniversity().getClass().getName());
//        Member member = new Member(joinDto.getNickname(), joinDto.getLoginId(), bCryptPasswordEncoder.encode(joinDto.getPassword()),
//                joinDto.getSex(), joinDto.getEmail(), "ROLE_USER", joinDto.getUniversity(), joinDto.getDept(), joinDto.getSno(), 0L);
        Member member = Member.builder()
                .nickname(joinDto.getNickname())
                .loginId(joinDto.getLoginId())
                .password(bCryptPasswordEncoder.encode(joinDto.getPassword()))
                .sex(joinDto.getSex())
                .roles("ROLE_USER")
                .email(joinDto.getEmail())
                .university(joinDto.getUniversity())
                .dept(joinDto.getDept())
                .sno(joinDto.getSno())
                .reliability(0L)
                .owner(false)      //방장은 기본 false
                .build();

        memberRepository.save(member);
        return true;
    }

    //아이디 중복검사
    @PostMapping("/duplicate-loginId")
    public Object duplicateId(@RequestBody @Validated @NotBlank String loginId) {
        log.info(loginId);
        if (memberRepository.findByLoginId(loginId).isPresent()) {
            return new ResponseEntity<Boolean>(false, HttpStatus.BAD_REQUEST);
        } else {
            return true;
        }
    }


    //닉네임 중복 검사
    @PostMapping("/duplicate-nickname")
    public Object duplicateNickName(@RequestBody @Validated @NotBlank String nickname) {
        log.info(nickname);
        log.info(nickname.getClass().getName());
        if (memberRepository.findByNickName(nickname).isPresent()) {
            return new ResponseEntity<Boolean>(false, HttpStatus.BAD_REQUEST);
        } else {
            return true;
        }
    }

    //메일로 url 보내기
    @PostMapping("/mail-auth")
    public Boolean mailAuthReq(@RequestBody @Email String email) {
        verifyCodeService.createVerifyCode(email);
        return true;
    }


    //메일 인증 결과
    @PostMapping("/mailcode-auth")
    public Object mailCodeAuth(@RequestBody String code) {
        log.info(code);
        Optional<VerifyCode> result = principalDetailService.confirmEmail(code);
        if (result.isEmpty()) {
            return new ResponseEntity<Boolean>(false, HttpStatus.BAD_REQUEST);
        }
        return true;
    }
}
