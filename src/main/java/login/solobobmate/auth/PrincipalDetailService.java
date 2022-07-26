package login.solobobmate.auth;

import login.solobobmate.auth.email.ConfirmationTokenService;
import login.solobobmate.auth.email.VerifyCodeService;
import login.solobobmate.domain.email.VerifyCode;
import login.solobobmate.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

//http://localhost:8080/login 요청시 동작 (spring security 기본 로그인 주소 => /login)

@Slf4j
@Service
@RequiredArgsConstructor
public class PrincipalDetailService implements UserDetailsService {

    private final MemberRepository memberRepository;
    private final ConfirmationTokenService confirmationTokenService;
    private final VerifyCodeService verifyCodeService;

    //return 될 때, securtiy session에 authentication 객체 넣어줌
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (memberRepository.findByLoginId(username).isPresent()) {
            return new PrincipalDetails(memberRepository.findByLoginId(username).get());
        } else {
            return null;
        }

    }

//    @Transactional
//    public Optional<ConfirmationToken> confirmEmail(String token) {
//        Optional<ConfirmationToken> confirmationToken = confirmationTokenService.findExpiredToken(token);
//        if (confirmationToken.isPresent()) {
//            confirmationToken.get().useToken();
//        }
//        return confirmationToken;
//    }

    @Transactional
    public Optional<VerifyCode> confirmEmail(String code) {
        Optional<VerifyCode> findcode = verifyCodeService.findExpiredCode(code);
        if (findcode.isPresent()) {
            findcode.get().useCode();
        }
        return findcode;
    }


}
