package login.jwtlogin.service;

import login.jwtlogin.domain.MatchingStatus;
import login.jwtlogin.domain.Member;
import login.jwtlogin.domain.Party;
import login.jwtlogin.domain.Restaurant;
import login.jwtlogin.repository.PartyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class PartyService {

    private final PartyRepository partyRepository;

    //파티 생성
    @Transactional  //그래야 멤버도 변경내용 저장됨
    public Party create(Member member, Restaurant restaurant, String title, int maxNumber) {
        Party party = Party.create(member, restaurant, title, maxNumber);
        partyRepository.save(party);
        return party;
    }

    // 파티 수정
    @Transactional
    public void update(Long id, String title, int maxNumber) {
        Party party = partyRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("파티를 찾을 수 없습니다")
        );
        party.update(title, maxNumber);
    }

    //파티 참가
    @Transactional
    public void join(Party party, Member member) {
        party.addMember(member);
    }

    // 파티 나가기
    @Transactional
    public void exit(Long id, Member member) {
        Party party = partyRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("파티를 찾을 수 없습니다")
        );
        party.deleteMember(member);
    }


}
