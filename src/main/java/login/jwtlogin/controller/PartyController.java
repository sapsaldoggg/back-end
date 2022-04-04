package login.jwtlogin.controller;

import login.jwtlogin.auth.PrincipalDetails;
import login.jwtlogin.controller.partyDto.PartyDto;
import login.jwtlogin.controller.partyDto.PartyOwnerDto;
import login.jwtlogin.controller.partyDto.PartyUpdateDto;
import login.jwtlogin.domain.Member;
import login.jwtlogin.domain.Party;
import login.jwtlogin.domain.Restaurant;
import login.jwtlogin.repository.PartyRepository;
import login.jwtlogin.repository.RestaurantRepository;
import login.jwtlogin.service.PartyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/user/restaurant/{restaurant_id}")
public class PartyController {

    private final PartyRepository partyRepository;
    private final PartyService partyService;

    private final RestaurantRepository restaurantRepository;

    //식당에 따른 파티 조회
    @GetMapping("/parties")
    public PartyOwnerDto partyList(@PathVariable(name = "restaurant_id") Long id, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        log.info("컨트롤러실행");
        log.info("restaurant_id = {}", id);
        Member member = principalDetails.getMember();

        Optional<Party> findParty = partyRepository.findPartyOwnerByMemberId(member);

        Restaurant restaurant = restaurantRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 식당입니다.")
        );
        List<Party> parties = partyRepository.findByRestaurantId(restaurant);
        List<PartyDto> partyListDtoList = new ArrayList<>();

        for (Party party : parties) {
            partyListDtoList.add(new PartyDto(party.getId(), party.getMember().getNickname(),
                    party.getRestaurant().getName(), party.getTitle(), party.getCreatedTime(),
                    party.getMatchingStatus(), party.getMaxNumber(), party.getCurrentNumber()));
        }
        return findParty.isPresent() ?
                new PartyOwnerDto(findParty.get().getId(), partyListDtoList) : new PartyOwnerDto(partyListDtoList);
    }

    //파티 생성
    @PostMapping("/party")
    public Boolean create(@AuthenticationPrincipal PrincipalDetails principalDetails,
                         @RequestBody PartyUpdateDto partyDto,
                         @PathVariable(name = "restaurant_id") Long id) {
        Member member = principalDetails.getMember();
        Restaurant restaurant = restaurantRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 식당입니다.")
        );
        partyService.create(member, restaurant, partyDto.getTitle(), partyDto.getMaxNumber());
        return true;
    }

    //파티 입장
    @GetMapping("/party/{party_id}")
    public PartyDto enter(@PathVariable(name = "party_id") Long id) {
        Party party = partyRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("파티가 존재하지 않습니다.")
        );
        return new PartyDto(party.getId(), party.getMember().getNickname(), party.getRestaurant().getName(),
                party.getTitle(), party.getCreatedTime(), party.getMatchingStatus(), party.getMaxNumber(), party.getCurrentNumber());
    }

    //파티 수정
    @PutMapping("/party/{party_id}")
    public Boolean edit(@RequestBody PartyUpdateDto partyDto, @PathVariable(name = "party_id") Long id) {
        partyService.update(id, partyDto.getTitle(), partyDto.getMaxNumber());
        return true;
    }



}
