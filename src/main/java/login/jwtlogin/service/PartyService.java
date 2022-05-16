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
    public Party join(Party party, Member member) {
        if (party.getFullStatus() == FullStatus.FULL || member.getIsJoined() || party.getMatchingStatus() == MatchingStatus.MATCHED) {
            return null;
        }
        party.addMember(member);
//        log.info("join (party) = {}", party);
        return party;
    }

    // 파티 나가기
    @Transactional
    public void exit(Long id, Member member) {
        Party party = partyRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("파티를 찾을 수 없습니다")
        );
        party.deleteMember(member);
    }

    @Transactional
    public void startOrReady(Party party, Member member) {
        if (member.getOwner() == true){  //방장인 경우
            if (member.getIsReady() == true) {  // 파티 시작 취소
                party.startCancelParty(member);
            }else{  // 파티 시작
                if (party.getMembers().size() >= 2) {  // 방장 포함 인원수 2 이상일때 가능
                    if (readyStatus(party) == true){
                        party.startParty(member);
                    }else{
                        throw new IllegalArgumentException("모두 준비 상태여야 합니다.");
                    }
                }else{
                    throw new IllegalArgumentException("파티원이 2명 미만입니다.");
                }
            }
        }else{      // 방장 아닌 경우
            if (member.getIsReady() == true) {  //파티 준비 취소
                if (party.getMatchingStatus() == MatchingStatus.MATCHED) {  //이미 시작이 된 경우
                    throw new IllegalArgumentException("파티가 이미 시작되었습니다.");
                }else{  //파티 준비 취소 작업 가능
                    party.cancelReady(member);
                }
            }else{  //파티 준비 작업
                party.ready(member);
            }
        }
    }

    //파티원이 모두 준비상태인지 확인 메소드
    public Boolean readyStatus(Party party) {
        for (Member member : party.getMembers()) {
            if (member.getIsReady() == false) {
                return false;
            }
        }
        return true;
    }

    //파티생성, 참가 시 파티정보(참가자 목록) 반환 메소드
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
