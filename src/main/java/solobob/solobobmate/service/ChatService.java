package solobob.solobobmate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import solobob.solobobmate.chat.chatDto.ChatMessageDto;
import solobob.solobobmate.controller.exception.ErrorCode;
import solobob.solobobmate.controller.exception.SoloBobException;
import solobob.solobobmate.domain.Chat;
import solobob.solobobmate.domain.ChatRoom;
import solobob.solobobmate.domain.Member;
import solobob.solobobmate.repository.ChatRepository;
import solobob.solobobmate.repository.ChatRoomRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRepository chatRepository;
    private final ChatRoomRepository chatRoomRepository;

    // 채팅 저장
    @Transactional
    public Chat create(Member member, ChatMessageDto chatMessageDto) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatMessageDto.getRoomId()).orElseThrow(
                () -> new SoloBobException(ErrorCode.NOT_FOUND_CHATROOM)
        );

        Chat chat = Chat.create(chatRoom, member.getNickname(), chatMessageDto.getMessage());
        chatRepository.save(chat);
        return chat;
    }


}
