package solobob.solobobmate.service;

import solobob.solobobmate.controller.memberDto.MyPageDto;
import solobob.solobobmate.domain.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import solobob.solobobmate.repository.MemberRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {


    public MyPageDto detail(Member member) {
        MyPageDto myPageDto = MyPageDto.builder()
                .nickname(member.getNickname())
                .sex(member.getSex())
                .university(member.getUniversity())
                .dept(member.getDept())
                .sno(member.getSno())
//                .reliability(member.getReliability())
                .build();

        return myPageDto;
    }

    @Transactional
    public void likeUp(Member member) {
        member.likeUp();
    }



}
