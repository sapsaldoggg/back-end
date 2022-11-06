package solobob.solobobmate.chat;

import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import solobob.solobobmate.auth.config.SecurityUtil;
import solobob.solobobmate.auth.jwt.TokenProvider;
import solobob.solobobmate.chat.chatDto.ChatMessageDto;
import solobob.solobobmate.chat.chatDto.ChatMessageResponseDto;
import solobob.solobobmate.chat.chatDto.LocationDto;
import solobob.solobobmate.controller.exception.ErrorCode;
import solobob.solobobmate.controller.exception.SoloBobException;
import solobob.solobobmate.domain.Chat;
import solobob.solobobmate.domain.Member;
import solobob.solobobmate.repository.MemberRepository;
import solobob.solobobmate.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;


@RequiredArgsConstructor
@RestController
@Slf4j
public class ChatController {

    private final TokenProvider tokenProvider;
    private final RedisPublisher redisPublisher;
    private final ChatService chatService;
    private final MemberRepository memberRepository;
    private final SimpMessagingTemplate template;

    public Member getMember() {
        Member member = memberRepository.findByLoginId(SecurityUtil.getCurrentMemberId()).orElseThrow(
                () -> new SoloBobException(ErrorCode.NOT_FOUND_MEMBER)
        );
        return member;
    }


    @MessageMapping("/party/message")  // /pub/party/message
    public void messageChat(@RequestBody ChatMessageDto chatMessageDto, SimpMessageHeaderAccessor accessor) {
        log.info("메시지: {}", chatMessageDto.getMessage());

        String jwtToken = accessor.getFirstNativeHeader("Authorization").substring(7);
        if (tokenProvider.validateToken(jwtToken)) {
            Authentication authentication = tokenProvider.getAuthentication(jwtToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        Member member = getMember();

        Chat chat = chatService.create(member, chatMessageDto);

        redisPublisher.publish(new ChannelTopic("/sub/chat/" + chatMessageDto.getRoomId()), new ChatMessageResponseDto(chat));
    }


    @MessageMapping("/party/position")
    public void chaseLocation(@RequestBody LocationDto locationDto, SimpMessageHeaderAccessor accessor){
        log.info("위도: {}", locationDto.getLatitude());
        log.info("경도: {}", locationDto.getLongitude());

        String jwtToken = accessor.getFirstNativeHeader("Authorization").substring(7);
        if (tokenProvider.validateToken(jwtToken)) {
            Authentication authentication = tokenProvider.getAuthentication(jwtToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        // 실시간 위치의 경우 redis 말고 Stomp 사용 -> RedisSubscriber에서 발행된 메시지를 ChatMessageResponseDto로 매핑하고 있기 때문
        log.info("locationDto room id: {}", locationDto.getRoomId());
        template.convertAndSend("/sub/position/"+locationDto.getRoomId(), locationDto);
    }


}
