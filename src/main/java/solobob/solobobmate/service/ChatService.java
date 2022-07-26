package solobob.solobobmate.service;

import solobob.solobobmate.domain.Chat;
import solobob.solobobmate.domain.Member;
import solobob.solobobmate.domain.Party;
import solobob.solobobmate.repository.ChatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRepository chatRepository;

    public Chat create(Party party, Member member, String message) {
        Chat chat = Chat.create(party, member, message);
        chatRepository.save(chat);
        return chat;
    }

}
