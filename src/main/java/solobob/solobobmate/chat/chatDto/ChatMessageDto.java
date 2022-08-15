package solobob.solobobmate.chat.chatDto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ChatMessageDto {

    private long roomId;

    private String sender;

    private String message;

    private LocalDateTime sendTime;

}
