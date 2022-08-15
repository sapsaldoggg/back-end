package solobob.solobobmate.service;

import org.springframework.beans.factory.annotation.Autowired;
import solobob.solobobmate.controller.exception.ErrorCode;
import solobob.solobobmate.controller.exception.SoloBobException;
import solobob.solobobmate.controller.memberDto.JoinDto;
import solobob.solobobmate.controller.memberDto.MyPageDto;
import solobob.solobobmate.domain.Authority;
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

    @Transactional
    public void likeUp(Long member_id) {
        Member findMember = memberRepository.findById(member_id).orElseThrow(
                () -> new SoloBobException(ErrorCode.NOT_FOUND_MEMBER)
        );
        findMember.likeUp();
    }



}
