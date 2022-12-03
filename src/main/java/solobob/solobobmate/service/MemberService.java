package solobob.solobobmate.service;

import solobob.solobobmate.controller.exception.ErrorCode;
import solobob.solobobmate.controller.exception.SoloBobException;
import solobob.solobobmate.controller.memberDto.MyPageDto;
import solobob.solobobmate.domain.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import solobob.solobobmate.repository.MemberRepository;

import static solobob.solobobmate.controller.exception.ErrorCode.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;


    public MyPageDto detail(String loginId) {
        Member member = memberRepository.findByLoginId(loginId).orElseThrow(
                () -> new SoloBobException(NOT_FOUND_MEMBER)
        );

        MyPageDto myPageDto = MyPageDto.builder()
                .nickname(member.getNickname())
                .sex(member.getSex())
                .university(member.getUniversity())
                .dept(member.getDept())
                .sno(member.getSno())
                .build();

        return myPageDto;
    }

    @Transactional
    public void likeUp(String loginId) {
        Member member = memberRepository.findByLoginId(loginId).orElseThrow(
                () -> new SoloBobException(NOT_FOUND_MEMBER)
        );
        member.likeUp();
    }



}
