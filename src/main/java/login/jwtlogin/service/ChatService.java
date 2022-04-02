package login.jwtlogin.service;

import login.jwtlogin.domain.Chat;
import login.jwtlogin.domain.Member;
import login.jwtlogin.domain.Party;
import login.jwtlogin.repository.ChatRepository;
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
