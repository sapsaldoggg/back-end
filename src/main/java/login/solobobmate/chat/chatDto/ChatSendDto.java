package login.solobobmate.chat.chatDto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ChatSendDto {

    private Long partyId;  //굳이 필요는 없음

    private String sender;

    private String message;

    private LocalDateTime sendTime;

}
