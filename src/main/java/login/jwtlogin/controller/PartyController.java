package login.jwtlogin.controller;

import login.jwtlogin.auth.PrincipalDetails;
import login.jwtlogin.controller.partyDto.PartyDto;
import login.jwtlogin.domain.Member;
import login.jwtlogin.domain.Party;
import login.jwtlogin.domain.Restaurant;
import login.jwtlogin.repository.PartyRepository;
import login.jwtlogin.repository.RestaurantRepository;
import login.jwtlogin.service.PartyService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user/restaurant/{restaurant_id}")
public class PartyController {

    private final PartyRepository partyRepository;
    private final PartyService partyService;

    private final RestaurantRepository restaurantRepository;

    //식당에 따른 파티 조회
    @GetMapping("/parties")
    public List<Party> partyList(@PathVariable(name = "restaurant_id") Long id) {
        return partyRepository.findByRestaurantId(id);
    }

    //파티 생성
    @PostMapping("/party")
    public String create(@AuthenticationPrincipal PrincipalDetails principalDetails,
                         @RequestBody PartyDto partyDto,
                         @PathVariable(name = "restaurant_id") Long id) {
        Member member = principalDetails.getMember();
        Restaurant restaurant = restaurantRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 식당입니다.")
        );
        partyService.create(member, restaurant, partyDto.getTitle(), partyDto.getMaxNumber());
        return "success";
    }

    //파티 입장
    @GetMapping("/party/{party_id}")
    public Party enter(@PathVariable(name = "party_id") Long id) {
        return partyRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("파티가 존재하지 않습니다.")
        );
    }

    //파티 수정
    @PutMapping("/party/{party_id}")
    public String edit(@RequestBody PartyDto partyDto, @PathVariable(name = "party_id") Long id) {
        partyService.update(id, partyDto.getTitle(), partyDto.getMaxNumber());
        return "success";
    }

}
