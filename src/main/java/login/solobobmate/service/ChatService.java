package login.solobobmate.service;

import login.solobobmate.domain.Chat;
import login.solobobmate.domain.Member;
import login.solobobmate.domain.Party;
import login.solobobmate.repository.ChatRepository;
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
