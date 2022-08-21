package solobob.solobobmate.chat;

import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.RequestBody;
import solobob.solobobmate.auth.config.SecurityUtil;
import solobob.solobobmate.chat.chatDto.ChatMessageDto;
import solobob.solobobmate.controller.exception.ErrorCode;
import solobob.solobobmate.controller.exception.SoloBobException;
import solobob.solobobmate.domain.Chat;
import solobob.solobobmate.domain.ChatRoom;
import solobob.solobobmate.domain.Member;
import solobob.solobobmate.domain.Party;
import solobob.solobobmate.repository.ChatRoomRepository;
import solobob.solobobmate.repository.MemberRepository;
import solobob.solobobmate.repository.PartyRepository;
import solobob.solobobmate.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@Slf4j
public class ChatController {

    private final RedisPublisher redisPublisher;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatService chatService;
    private final MemberRepository memberRepository;

    public Member getMember() {
        Member member = memberRepository.findByLoginId(SecurityUtil.getCurrentMemberId()).orElseThrow(
                () -> new SoloBobException(ErrorCode.NOT_FOUND_MEMBER)
        );
        return member;
    }



    @MessageMapping("/party/message")  // /pub/party/message
    public void messageChat(@RequestBody ChatMessageDto chatMessageDto) {
        Member member = getMember();
        ChatRoom chatRoom = chatRoomRepository.findById(chatMessageDto.getRoomId()).orElseThrow(
                () -> new SoloBobException(ErrorCode.NOT_FOUNT_CHATROOM)
        );

        Chat chat = chatService.create(chatRoom, member, chatMessageDto);

        redisPublisher.publish(new ChannelTopic("ㅈ까"), chatMessageDto);
    }


}
