package solobob.solobobmate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import solobob.solobobmate.chat.chatDto.ChatMessageDto;
import solobob.solobobmate.domain.Chat;
import solobob.solobobmate.domain.ChatRoom;
import solobob.solobobmate.domain.Member;
import solobob.solobobmate.repository.ChatRepository;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRepository chatRepository;

    // 채팅 저장
    @Transactional
    public Chat create(ChatRoom chatRoom, Member member, ChatMessageDto chatMessageDto) {
        Chat chat = Chat.create(chatRoom, member.getNickname(), chatMessageDto.getMessage());
        chatRepository.save(chat);
        return chat;
    }


}
