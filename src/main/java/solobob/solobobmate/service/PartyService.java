package solobob.solobobmate.service;

import solobob.solobobmate.controller.exception.ExceptionMessages;
import solobob.solobobmate.controller.exception.party.PartyException;
import solobob.solobobmate.controller.exception.party.StartOrReadyException;
import solobob.solobobmate.controller.partyDto.PartyDto;
import solobob.solobobmate.domain.*;
import solobob.solobobmate.repository.PartyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Service
@Slf4j
@RequiredArgsConstructor
public class PartyService {

    private final PartyRepository partyRepository;

    //파티 생성
    @Transactional
    public Party create(Member member, Restaurant restaurant, String title, int maxNumber) {
        if (member.getIsJoined() == true) {
            throw new PartyException("이미 파티에 소속되어있습니다");
        }
        Party party = Party.create(member, restaurant, title, maxNumber);
        partyRepository.save(party);
        return party;
    }

    // 파티 수정
    @Transactional
    public void update(Long id, String title, int maxNumber) {
        Party party = partyRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(ExceptionMessages.NOT_FOUND_PARTY)
        );
        party.update(title, maxNumber);
    }

    //파티 참가
    @Transactional
    public Party join(Party party, Member member) {
        if (party.getFullStatus() == FullStatus.FULL || member.getIsJoined()
                || party.getMatchingStatus() == MatchingStatus.MATCHED) {
            throw new PartyException("참가할 수 없습니다.");
        }
        party.addMember(member);

        return party;
    }

    // 파티 나가기 (멤버만 가능, 방장x)
    @Transactional
    public void exit(Party party, Member member) {
        if (member.getOwner() == true) {
            throw new PartyException("방장은 나갈수 없습니다.");
        } else if (party.getMatchingStatus() == MatchingStatus.MATCHED) {
            throw new PartyException("이미 파티가 매칭되었습니다.");
        } else if (member.getIsReady() == true) {
            throw new PartyException("준비상태에서 파티를 나갈 수 없습니다.");
        }
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
                        throw new StartOrReadyException("모두 준비 상태여야 합니다.");
                    }
                }else{
                    throw new StartOrReadyException("파티원이 2명 미만입니다.");
                }
            }
        }else{      // 방장 아닌 경우
            if (member.getIsReady() == true) {  //파티 준비 취소
                if (party.getMatchingStatus() == MatchingStatus.MATCHED) {  //이미 시작이 된 경우
                    throw new StartOrReadyException("파티가 이미 시작되었습니다.");
                }else{  //파티 준비 취소 작업 가능
                    party.cancelReady(member);
                }
            }else{  //파티 준비 작업
                party.ready(member);
            }
        }
    }

    //파티원이 모두 준비상태인지 확인 메소드 (방장을 제외한)
    public Boolean readyStatus(Party party) {
        for (Member member : party.getMembers()) {
            if (member.getOwner() == true) {
                continue;
            }
            if (member.getIsReady() == false) {
                return false;
            }
        }
        return true;
    }

    //파티생성, 참가 시 파티정보(참가자 목록) 반환 메소드
    public PartyDto partyInfoReturn(Party party) {

        return new PartyDto(party);

    }


    //파티 삭제 (방장 권한)
    @Transactional
    public void initialMembers(Member member, Party party) {
        if (member.getOwner() == false) {
            throw new PartyException("방장 권한 입니다");
        }
        for (Member partyMember : party.getMembers()) {
            if (partyMember.getOwner() == true) { //프록시 객체 초기화 => (1)+1
                partyMember.setOwner(false);
            }
            partyMember.setParty(null);
            partyMember.setIsJoined(false);
            partyMember.setIsReady(false);
        }
        party.getMembers().clear();
        partyRepository.removeParty(party);
    }

//    @Transactional
//    public void removePartyScheduler() {
//        List<Party> parties = partyRepository.findAll();
//        for (Party party : parties) {
//            if (party.getMatchingStatus() == MatchingStatus.MATCHED) {
//                Duration duration = Duration.between(party.getMatchingStartTime(), LocalDateTime.now());
//                if (duration.getSeconds() >= 7200) {   //2시간 지나면
//                    initialMembers(party);
//                }
//            }
//        }
//    }


}
