package solobob.solobobmate.chat.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import solobob.solobobmate.chat.chatDto.ChatSendDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisSubscriber implements MessageListener {

    private final ObjectMapper objectMapper;
    private final RedisTemplate redisTemplate;
    private final SimpMessageSendingOperations messageTemplate;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            String publishMessage = (String) redisTemplate.getStringSerializer().deserialize(message.getBody());
            ChatSendDto chatSendDto = objectMapper.readValue(publishMessage, ChatSendDto.class);
            messageTemplate.convertAndSend("/sub/party/"+chatSendDto.getPartyId());
            messageTemplate.convertAndSend("/sub/party/"+chatSendDto.getPartyId()+"/chat");
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
