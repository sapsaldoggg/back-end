package solobob.solobobmate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import solobob.solobobmate.auth.config.SecurityUtil;
import solobob.solobobmate.auth.jwt.TokenDto;
import solobob.solobobmate.auth.jwt.TokenProvider;
import solobob.solobobmate.controller.memberDto.JoinDto;
import solobob.solobobmate.controller.memberDto.LoginDto;
import solobob.solobobmate.domain.Authority;
import solobob.solobobmate.domain.Member;
import solobob.solobobmate.domain.email.VerifyCode;
import solobob.solobobmate.repository.MemberRepository;
import solobob.solobobmate.service.email.VerifyCodeService;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final VerifyCodeService verifyCodeService;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final TokenProvider tokenProvider;


    public String getLoginId() {
        return SecurityUtil.getCurrentMemberId();
    }

    /**
     * 회원가입
     * @param joinDto
     */
    @Transactional
    public void save(JoinDto joinDto) {
        Member member = Member.builder()
                .nickname(joinDto.getNickname())
                .loginId(joinDto.getLoginId())
                .password(passwordEncoder.encode(joinDto.getPassword()))
                .sex(joinDto.getSex())
                .authority(Authority.ROLE_USER)
                .email(joinDto.getEmail())
                .university(joinDto.getUniversity())
                .dept(joinDto.getDept())
                .sno(joinDto.getSno())
                .reliability(0L)   //신뢰도
                .owner(false)      //방장은 기본 false
                .isJoined(false)
                .isReady(false)
                .build();

        memberRepository.save(member);
    }

    /**
     * 로그인
     * @param loginDto
     * @return
     */
    public TokenDto login(LoginDto loginDto){

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginDto.getLoginId(), loginDto.getPassword());

        Authentication authenticate = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        TokenDto tokenDto = tokenProvider.generateTokenDto(authenticate);

        return tokenDto;
    }

    public TokenDto refresh(String refreshToken) {
        Authentication authentication = tokenProvider.getAuthentication(refreshToken);

        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);

        return tokenDto;
    }

    /**
     * 이메일 인증 코드 확인
     * @param code
     * @return
     */
    @Transactional
    public Optional<VerifyCode> confirmEmail(String code) {
        Optional<VerifyCode> findCode = verifyCodeService.findExpiredCode(code);
        if (findCode.isPresent()) {
            findCode.get().useCode();
        }
        return findCode;
    }

}
