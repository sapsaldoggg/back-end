package solobob.solobobmate.controller;


import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import solobob.solobobmate.auth.config.SecurityUtil;
import solobob.solobobmate.controller.exception.ErrorCode;
import solobob.solobobmate.controller.exception.SoloBobException;
import solobob.solobobmate.controller.partyDto.*;
import solobob.solobobmate.controller.reportDto.ReportDto;
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
import solobob.solobobmate.service.ReportService;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class PartyController {

    private final MemberRepository memberRepository;

    private final PartyRepository partyRepository;
    private final PartyService partyService;
    private final ReportService reportService;

    private final RestaurantRepository restaurantRepository;


    public Member getMember(){
        Member member = memberRepository.findByLoginId(SecurityUtil.getCurrentMemberId()).orElseThrow(
                () -> new SoloBobException(ErrorCode.NOT_FOUND_MEMBER)
        );
        return member;
    }

    // 전체 파티 목록 조회
    @GetMapping("/parties")
    public ResponseEntity parties(@PageableDefault(size=10, sort = "createAt", direction = Sort.Direction.DESC) Pageable pageable){

        List<Party> parties = partyRepository.findPartiesByCreatedDateDesc(pageable);

        return ResponseEntity.ok(new PartyListDto(parties));
    }

    // 키워드로 파티 조회
    @GetMapping("/party/search")
    public ResponseEntity findPartyWithKeyword(@RequestParam(name = "keyword") String keyword
            ,@PageableDefault(size=10, sort = "createAt", direction = Sort.Direction.DESC) Pageable pageable){

        List<Party> parties = partyRepository.findWithKeyword(keyword, pageable);

        return ResponseEntity.ok(new PartyListDto(parties));
    }


    //식당에 따른 파티 조회
    @GetMapping("/restaurant/{restaurant_id}/parties")
    public ResponseEntity partyList(@PathVariable(name = "restaurant_id") Long id) {

        List<Party> parties = partyRepository.findWithRestaurant(id);

        return ResponseEntity.ok(new PartyListDto(parties));
    }


    //파티 생성
    @PostMapping("/restaurant/{restaurant_id}/party")
    public ResponseEntity create(@RequestBody @Validated PartyCreateDto partyDto,
                                 @PathVariable(name = "restaurant_id") Long id) {
        Member member = getMember();

        Restaurant restaurant = restaurantRepository.findById(id).orElseThrow(
                () -> new SoloBobException(ErrorCode.NOT_FOUND_RESTAURANT)
        );

        Party party = partyService.create(member, restaurant, partyDto.getTitle(), partyDto.getMaximumCount());

        return ResponseEntity.ok(partyService.partyInfoReturn(party));

    }

    //파티 참가
    @PostMapping("/restaurant/{restaurant_id}/party/{party_id}/join")
    public ResponseEntity joinParty(@PathVariable(name = "party_id") Long id) {
        Member member = getMember();

        Party party = partyRepository.findWithAllById(id).orElseThrow(
                () -> new SoloBobException(ErrorCode.NOT_FOUND_PARTY)
        );

        return ResponseEntity.ok(partyService.join(party, member));

    }


    //파티 나가기 (멤버만 가능, 방장 x)
    @PostMapping("/restaurant/{restaurant_id}/party/{party_id}/exit")
    public void exitParty(@PathVariable(name = "party_id") Long id) {
        Member member = getMember();

        Party party = partyRepository.findWithAllById(id).orElseThrow(
                () -> new SoloBobException(ErrorCode.NOT_FOUND_PARTY)
        );

        Member owner = memberRepository.findByNickname(party.getOwner()).orElseThrow(
                () -> new SoloBobException(ErrorCode.NOT_FOUND_MEMBER)
        );

        partyService.exit(party, member, owner);
    }


    //파티 수정
    //인원수 늘리기만 가능
    @PutMapping("/restaurant/{restaurant_id}/party/{party_id}")
    public void edit(@RequestBody PartyCreateDto partyDto, @PathVariable(name = "party_id") Long id) {
        Party party = partyRepository.findById(id).orElseThrow(
                () -> new SoloBobException(ErrorCode.NOT_FOUND_PARTY)
        );
        partyService.update(party, partyDto.getTitle(), partyDto.getMaximumCount());

    }

    // 파티 준비 or 시작 - 추가쿼리발생x
    @PostMapping("/restaurant/{restaurant_id}/party/{party_id}/ready")
    public ResponseEntity ready(@PathVariable("party_id") Long id) {
        Member member = getMember();

        Party party = partyRepository.findById(id).orElseThrow(
                () -> new SoloBobException(ErrorCode.NOT_FOUND_PARTY)
        );

        partyService.startOrReady(party, member);

        return ResponseEntity.ok(partyService.partyInfoReturn(party));
    }

    // 파티 삭제 (방장권한)
    @DeleteMapping("/restaurant/{restaurant_id}/party/{party_id}")
    public void delete(@PathVariable("party_id") Long id) {
        Member member = getMember();
        log.info("id ={}", id);
        Party party = partyRepository.findWithAllById(id).orElseThrow(
                () -> new SoloBobException(ErrorCode.NOT_FOUND_PARTY)
        );
        log.info("============================");
        partyService.initialMembers(member, party);
    }


    // 파티원 강퇴 (방장권한)
    @PostMapping("/restaurant/{restaurant_id}/party/{party_id}/kick-out")
    public ResponseEntity kickOut(@PathVariable("party_id") Long id, @RequestBody Map<String, Long> memberIdMap){
        Member member = getMember();

        Member kickMember = memberRepository.findById(memberIdMap.get("memberId")).orElseThrow(
                () -> new SoloBobException(ErrorCode.NOT_FOUND_MEMBER)
        );

        Party party = partyRepository.findById(id).orElseThrow(
                () -> new SoloBobException(ErrorCode.NOT_FOUND_PARTY)
        );

        partyService.kickOutMember(member, kickMember, party);

        return ResponseEntity.ok(partyService.partyInfoReturn(party));
    }


    // 파티원 신고
    @PostMapping("/restaurant/{restaurant_id}/party/{party_id}/report")
    public ResponseEntity reportMember(@PathVariable("party_id") Long id, @RequestBody ReportDto reportDto){
        Member fromMember = getMember();

        Member toMember = memberRepository.findById(reportDto.getMemberId()).orElseThrow(
                () -> new SoloBobException(ErrorCode.NOT_FOUND_MEMBER)
        );

        Party party = partyRepository.findById(id).orElseThrow(
                () -> new SoloBobException(ErrorCode.NOT_FOUND_PARTY)
        );

        reportService.reportMember(fromMember, toMember, party, reportDto);

        return ResponseEntity.ok(true);
    }


    // 파티원 신고 내역 조회
    @GetMapping("/restaurant/{restaurant_id}/party/{party_id}/reports")
    public ResponseEntity memberReports(@PathVariable("party_id") Long id, @RequestParam(name = "memberId") Long memberId){
        Member inquiryMember = memberRepository.findById(memberId).orElseThrow(
                () -> new SoloBobException(ErrorCode.NOT_FOUND_MEMBER)
        );

        Party party = partyRepository.findById(id).orElseThrow(
                () -> new SoloBobException(ErrorCode.NOT_FOUND_PARTY)
        );

        return ResponseEntity.ok(reportService.reportInfoReturn(inquiryMember, party));
    }

}
