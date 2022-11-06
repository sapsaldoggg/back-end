package solobob.solobobmate.chat;


import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;
import solobob.solobobmate.chat.chatDto.ChatMessageResponseDto;

@RequiredArgsConstructor
@Service
public class RedisPublisher {
    private final RedisTemplate<String, Object> redisTemplate;

    public void publish(ChannelTopic topic, ChatMessageResponseDto message) {
        redisTemplate.convertAndSend(topic.getTopic(), message);
    }
}
