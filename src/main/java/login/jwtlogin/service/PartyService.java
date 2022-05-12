package login.jwtlogin.service;

import login.jwtlogin.controller.partyDto.PartyDto;
import login.jwtlogin.controller.partyDto.PartyMembersDto;
import login.jwtlogin.domain.*;
import login.jwtlogin.repository.PartyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
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

    public PartyDto partyInfoReturn(Party party) {
        List<PartyMembersDto> members = new ArrayList<>();

        for (Member partyMember : party.getMembers()) {
            members.add(new PartyMembersDto(partyMember.getNickname(), partyMember.getSex(), partyMember.getDept(),
                    partyMember.getSno(), partyMember.getReliability()));
        }
        return new PartyDto(party.getId(), party.getOwner(), party.getRestaurant().getName(),
                party.getTitle(), party.getCreatedTime(), party.getMatchingStatus(), party.getMaxNumber(),
                party.getCurrentNumber(), members);
    }


}
