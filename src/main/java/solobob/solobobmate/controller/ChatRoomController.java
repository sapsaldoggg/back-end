package solobob.solobobmate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import solobob.solobobmate.chat.RedisSubscriber;
import solobob.solobobmate.controller.chatDto.ChatRoomDto;
import solobob.solobobmate.controller.exception.ErrorCode;
import solobob.solobobmate.controller.exception.SoloBobException;
import solobob.solobobmate.domain.Chat;
import solobob.solobobmate.domain.Party;
import solobob.solobobmate.repository.ChatRepository;
import solobob.solobobmate.repository.PartyRepository;

import java.util.Comparator;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/user/restaurant/{restaurant_id}/party/{party_id}")
public class ChatRoomController {

    private final PartyRepository partyRepository;
    private final ChatRepository chatRepository;
    private final RedisSubscriber redisSubscriber;
    private final RedisMessageListenerContainer redisMessageListenerContainer;


    @GetMapping("/chat")
    public ResponseEntity chats(@PathVariable(name = "party_id") Long id,
                                @PageableDefault(size=50, sort = "createAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Party party = partyRepository.findById(id).orElseThrow(
                () -> new SoloBobException(ErrorCode.NOT_FOUND_PARTY)
        );

        ChannelTopic topic = new ChannelTopic("/sub/chat/" + party.getChatRoom().getId());
        redisMessageListenerContainer.addMessageListener(redisSubscriber, topic);

        List<Chat> chats = chatRepository.findChatsByCreatedDateDesc(party.getChatRoom().getId(), pageable);
        chats.sort(Comparator.comparing(Chat::getCreateAt));


        return ResponseEntity.ok(new ChatRoomDto(party.getChatRoom().getId(), chats));
    }

}
