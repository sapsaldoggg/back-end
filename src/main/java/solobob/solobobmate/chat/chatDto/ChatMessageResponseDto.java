package solobob.solobobmate.chat.chatDto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Data;
import lombok.NoArgsConstructor;
import solobob.solobobmate.domain.Chat;

import java.time.LocalDateTime;

@NoArgsConstructor
@Data
public class ChatMessageResponseDto {

    private Long chatId;

    private String sender;

    private String message;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime sendTime;

    public ChatMessageResponseDto(Chat chat){
        this.chatId = chat.getId();
        this.sender = chat.getSender();
        this.message = chat.getMessage();
        this.sendTime = chat.getCreateAt();
    }
}
