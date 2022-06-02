package login.jwtlogin.controller;

import login.jwtlogin.auth.PrincipalDetails;
import login.jwtlogin.controller.exception.ExceptionMessages;
import login.jwtlogin.controller.partyDto.*;
import login.jwtlogin.domain.FullStatus;
import login.jwtlogin.domain.Member;
import login.jwtlogin.domain.Party;
import login.jwtlogin.domain.Restaurant;
import login.jwtlogin.repository.MemberRepository;
import login.jwtlogin.repository.PartyRepository;
import login.jwtlogin.repository.RestaurantRepository;
import login.jwtlogin.service.PartyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
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


    //식당에 따른 파티 조회
    @GetMapping("/parties")
    public PartyListDto partyList(@PathVariable(name = "restaurant_id") Long id, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        log.info("컨트롤러실행");
        log.info("restaurant_id = {}", id);
        Member member = principalDetails.getMember();

        Optional<Party> findParty = partyRepository.findByOwnerNickName(member.getNickname());

        Restaurant restaurant = restaurantRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(ExceptionMessages.NOTFOUND_RESTAURANT)
        );

        List<Party> parties = partyRepository.findByRestaurantId(restaurant);

        List<PartyInfoDto> partyListDtoList = new ArrayList<>();


        //
        for (Party party : parties) {
            partyListDtoList.add(new PartyInfoDto(party.getId(), party.getOwner(),
                    party.getRestaurant().getName(), party.getTitle(), party.getCreatedTime(),
                    party.getMatchingStatus(), party.getMaxNumber(), party.getCurrentNumber()));
        }

        return findParty.isPresent() ?
                new PartyListDto(findParty.get().getId(), partyListDtoList) : new PartyListDto(partyListDtoList);
    }

    //파티 생성
    @PostMapping("/party")
    public Object create(@AuthenticationPrincipal PrincipalDetails principalDetails,
                         @RequestBody PartyCreateDto partyDto,
                         @PathVariable(name = "restaurant_id") Long id) {
        Member member = principalDetails.getMember();
        Member findMember = memberRepository.findById(member.getId()).orElseThrow(
                () -> new EntityNotFoundException(ExceptionMessages.NOT_FOUND_MEMBER)
        );
        Restaurant restaurant = restaurantRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(ExceptionMessages.NOTFOUND_RESTAURANT)
        );
        if (findMember.getIsJoined() == true) {
            return new ResponseEntity<>("이미 파티에 소속되어 있습니다", HttpStatus.BAD_REQUEST);
        }
        Party party = partyService.create(findMember, restaurant, partyDto.getTitle(), partyDto.getMaxNumber());

        return partyService.partyInfoReturn(party);

    }

    //파티 참가
    @PostMapping("/party/{party_id}/join")
    public Object joinParty(@PathVariable(name = "party_id") Long id, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        Member member = principalDetails.getMember();
        Member findMember = memberRepository.findById(member.getId()).orElseThrow(
                () -> new EntityNotFoundException(ExceptionMessages.NOT_FOUND_MEMBER)
        );
        Party party = partyRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(ExceptionMessages.NOTFOUND_PARTY)
        );

        if (partyService.join(party, findMember) == null) {
            return new ResponseEntity<Boolean>(false, HttpStatus.BAD_REQUEST);
        }
        partyService.join(party, findMember);
        return partyService.partyInfoReturn(party);
    }

    //파티 나가기 (멤버만 가능, 방장 x)
    @PostMapping("/party/{party_id}/exit")
    public void exitParty(@PathVariable(name = "party_id") Long id, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        Member member = principalDetails.getMember();
        Member findMember = memberRepository.findById(member.getId()).orElseThrow(
                () -> new EntityNotFoundException(ExceptionMessages.NOT_FOUND_MEMBER)
        );
        partyService.exit(id, findMember);
    }

    //파티 입장
    @GetMapping("/party/{party_id}")
    public PartyDto enter(@PathVariable(name = "party_id") Long id) {
        Party party = partyRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(ExceptionMessages.NOTFOUND_PARTY)
        );
        return partyService.partyInfoReturn(party);
    }

    //파티 수정
    @PutMapping("/party/{party_id}")
    public Boolean edit(@RequestBody PartyCreateDto partyDto, @PathVariable(name = "party_id") Long id) {
        partyService.update(id, partyDto.getTitle(), partyDto.getMaxNumber());
        return true;
    }

    //파티 준비 or 시작
    @PostMapping("/party/{party_id}/ready")
    public void ready(@PathVariable("party_id") Long id, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        Member member = principalDetails.getMember();
        Member findMember = memberRepository.findById(member.getId()).orElseThrow(
                () -> new EntityNotFoundException(ExceptionMessages.NOT_FOUND_MEMBER)
        );
        Party party = partyRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(ExceptionMessages.NOTFOUND_PARTY)
        );
        partyService.startOrReady(party, findMember);
    }

    @DeleteMapping("/party/{party_id}")
    public void delete(@PathVariable("party_id") Long id, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        Member member = principalDetails.getMember();
        Member findMember = memberRepository.findById(member.getId()).orElseThrow(
                () -> new EntityNotFoundException(ExceptionMessages.NOT_FOUND_MEMBER)
        );
        Party party = partyRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(ExceptionMessages.NOTFOUND_PARTY)
        );

        if (findMember.getOwner() == false) {
            throw new IllegalStateException("방장 권한 입니다.");
        }
        partyService.initialMembers(party);
    }


//
}
