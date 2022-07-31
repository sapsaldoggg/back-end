package solobob.solobobmate.controller;


import solobob.solobobmate.auth.config.SecurityUtil;
import solobob.solobobmate.controller.exception.ExceptionMessages;
import solobob.solobobmate.controller.partyDto.*;
import solobob.solobobmate.domain.Member;
import solobob.solobobmate.domain.Party;
import solobob.solobobmate.domain.Restaurant;
import solobob.solobobmate.repository.MemberRepository;
import solobob.solobobmate.repository.PartyRepository;
import solobob.solobobmate.repository.RestaurantRepository;
import solobob.solobobmate.service.PartyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/user/restaurant/{restaurant_id}")
public class PartyController {

    private final MemberRepository memberRepository;

    private final PartyRepository partyRepository;
    private final PartyService partyService;

    private final RestaurantRepository restaurantRepository;


    public Member getMember() {
        Member member = memberRepository.findByLoginId(SecurityUtil.getCurrentMemberId()).orElseThrow(
                () -> new EntityNotFoundException(ExceptionMessages.NOT_FOUND_MEMBER)
        );
        return member;
    }

    //식당에 따른 파티 조회
    @GetMapping("/parties")
    public ResponseEntity partyList(@PathVariable(name = "restaurant_id") Long id) {

        Member member = getMember();

        Optional<Party> findParty = partyRepository.findByOwnerNickName(member.getNickname());

        List<Party> parties = partyRepository.findWithRestaurant(id);

        return findParty.isPresent() ?
                ResponseEntity.ok(new PartyListDto(findParty.get().getId(), parties)) : ResponseEntity.ok(new PartyListDto(parties));
    }

    //파티 생성
    @PostMapping("/party")
    public ResponseEntity create(@RequestBody @Validated PartyCreateDto partyDto,
                         @PathVariable(name = "restaurant_id") Long id) {
        Member member = getMember();

        Restaurant restaurant = restaurantRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(ExceptionMessages.NOT_FOUND_RESTAURANT)
        );

        Party party = partyService.create(member, restaurant, partyDto.getTitle(), partyDto.getMaximumCount());

        return ResponseEntity.ok(partyService.partyInfoReturn(party));

    }

    //파티 참가
    @PostMapping("/party/{party_id}/join")
    public ResponseEntity joinParty(@PathVariable(name = "party_id") Long id) {
        Member member = getMember();

        Party party = partyRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(ExceptionMessages.NOT_FOUND_PARTY)
        );

        partyService.join(party, member);

        return ResponseEntity.ok(partyService.partyInfoReturn(party));
    }

    //파티 나가기 (멤버만 가능, 방장 x)
    @PostMapping("/party/{party_id}/exit")
    public void exitParty(@PathVariable(name = "party_id") Long id) {

        Member member = getMember();

        Party party = partyRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(ExceptionMessages.NOT_FOUND_PARTY)
        );

        partyService.exit(party, member);
    }

    //파티 입장
    @GetMapping("/party/{party_id}")
    public ResponseEntity enter(@PathVariable(name = "party_id") Long id) {
        Member member = getMember();
        Party party = partyRepository.findWithMembersRestaurant(id).orElseThrow(
                () -> new EntityNotFoundException(ExceptionMessages.NOT_FOUND_PARTY)
        );

        return ResponseEntity.ok(partyService.partyInfoReturn(party));
    }

    //파티 수정
    @PutMapping("/party/{party_id}")
    public void edit(@RequestBody PartyCreateDto partyDto, @PathVariable(name = "party_id") Long id) {

        partyService.update(id, partyDto.getTitle(), partyDto.getMaximumCount());

    }

    // 파티 준비 or 시작
    @PostMapping("/party/{party_id}/ready")
    public void ready(@PathVariable("party_id") Long id) {
        Member member = getMember();

        Party party = partyRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(ExceptionMessages.NOT_FOUND_PARTY)
        );

        partyService.startOrReady(party, member);
    }

    // 파티 삭제 (방장권한)
    @DeleteMapping("/party/{party_id}")
    public void delete(@PathVariable("party_id") Long id) {
        Member member = getMember();

        Party party = partyRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(ExceptionMessages.NOT_FOUND_PARTY)
        );

        partyService.initialMembers(member, party);
    }



}
