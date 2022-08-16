package solobob.solobobmate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import solobob.solobobmate.controller.exception.ErrorCode;
import solobob.solobobmate.controller.exception.SoloBobException;
import solobob.solobobmate.domain.Party;
import solobob.solobobmate.repository.PartyRepository;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/user/restaurant/{restaurant_id}/party/{party_id}")
public class ChatRoomController {

    private final PartyRepository partyRepository;

    // 채팅창 화면 조회
    @GetMapping("/chat")
    public ResponseEntity chatRoom(@PathVariable(name = "party_id") Long id) {
        Party party = partyRepository.findById(id).orElseThrow(
                () -> new SoloBobException(ErrorCode.NOT_FOUND_PARTY)
        );
        return null;
    }

}
