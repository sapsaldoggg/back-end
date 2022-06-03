package login.jwtlogin.chat;

import login.jwtlogin.auth.PrincipalDetails;
import login.jwtlogin.chat.chatDto.ChatDto;
import login.jwtlogin.chat.chatDto.ChatSendDto;
import login.jwtlogin.controller.exception.ExceptionMessages;
import login.jwtlogin.domain.Chat;
import login.jwtlogin.domain.Member;
import login.jwtlogin.domain.Party;
import login.jwtlogin.repository.PartyRepository;
import login.jwtlogin.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@Slf4j
public class ChatController {

    private final SimpMessagingTemplate template;
    private final PartyRepository partyRepository;
    private final ChatService chatService;

    // /pub/party/enter 로 요청이 오면, 파티 입장
//    @MessageMapping("/party/enter")   // /pub/party/enter
//    public void enterChat(@RequestBody Long partyId, @AuthenticationPrincipal PrincipalDetails principalDetails){
//        Member member = principalDetails.getMember();
//        Party party = partyRepository.findById(partyId).orElseThrow(
//                () -> new IllegalArgumentException("파티가 존재하지 않습니다")
//        );
//        Chat chat = chatService.create(party, member, member.getNickname() + "님이 입장하였습니다.");
//
//        ChatSendDto chatSendDto = ChatSendDto.builder()
//                .partyId(chat.getParty().getId())
//                .sender(chat.getMember().getNickname())
//                .message(chat.getMessage())
//                .sendTime(chat.getSendTime())
//                .build();
//
//        template.convertAndSend("/sub/party/"+partyId, chatSendDto);
//    }

    // /pub/party/message 로 요청이 오면, 메시지 전송 (SEND)
    @MessageMapping("/party/message")  // /pub/party/message
    public void messageChat(@RequestBody ChatDto chatDto, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        Member member = principalDetails.getMember();
        Party party = partyRepository.findById(chatDto.getPartyId()).orElseThrow(
                () -> new IllegalArgumentException(ExceptionMessages.NOTFOUND_PARTY)
        );
        Chat chat = chatService.create(party, member, chatDto.getMessage());

        ChatSendDto chatSendDto = ChatSendDto.builder()
                .partyId(chat.getParty().getId())
                .sender(chat.getMember().getNickname())
                .message(chat.getMessage())
                .sendTime(chat.getSendTime())
                .build();

        template.convertAndSend("/sub/party/" + chatDto.getPartyId()+"/chat", chatSendDto);
    }
}
