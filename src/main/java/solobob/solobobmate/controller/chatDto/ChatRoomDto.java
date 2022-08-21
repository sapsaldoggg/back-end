package solobob.solobobmate.controller.chatDto;

import lombok.Data;
import solobob.solobobmate.domain.Chat;
import solobob.solobobmate.domain.Party;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class ChatRoomDto {

    private Long chatRoomId;

    private List<ChatDto> chats;

    public ChatRoomDto(Party party) {
        this.chatRoomId = party.getChatRoom().getId();
        this.chats = party.getChatRoom().getChats().stream().map(chat -> new ChatDto(chat)).collect(Collectors.toList());
    }
}
