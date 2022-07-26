package solobob.solobobmate.service;

import solobob.solobobmate.controller.memberDto.JoinDto;
import solobob.solobobmate.controller.memberDto.MyPageDto;
import solobob.solobobmate.domain.Member;
import solobob.solobobmate.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Transactional
    public void save(JoinDto joinDto) {
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
                .isJoined(false)
                .isReady(false)
                .build();

        memberRepository.save(member);
    }

    public MyPageDto detail(Member member) {
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


}
