package solobob.solobobmate.controller.chatDto;

import lombok.Data;
import solobob.solobobmate.domain.Chat;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class ChatRoomDto {

    private Long chatRoomId;

    private List<ChatDto> chats;

    public ChatRoomDto(Long chatRoomId, List<Chat> chats){
        this.chatRoomId = chatRoomId;
        this.chats = chats.stream().map(chat -> new ChatDto(chat)).collect(Collectors.toList());
    }
}
