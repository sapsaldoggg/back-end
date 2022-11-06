package solobob.solobobmate.chat;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.StompSubProtocolErrorHandler;
import solobob.solobobmate.controller.exception.ErrorCode;

import java.nio.charset.StandardCharsets;

@Slf4j
@Component
public class ChatErrorHandler extends StompSubProtocolErrorHandler {

    public ChatErrorHandler() {
        super();
    }

    @Override
    public Message<byte[]> handleClientMessageProcessingError(Message<byte[]> clientMessage, Throwable ex) {

        if (ex.getCause().getMessage().equals("JWT")){
            return handleJwtException(clientMessage, ex);
        }
        if (ex.getCause().getMessage().equals("Auth")){
            return handleUnauthorizedException(clientMessage, ex);
        }

        return super.handleClientMessageProcessingError(clientMessage, ex);
    }

    // 권한 예외 (해당 방에 접속하지 않았을 시)
    private Message<byte[]> handleJwtException(Message<byte[]> clientMessage, Throwable ex) {
        return prepareErrorMessage(ErrorCode.PARTY_MY_PARTY);
    }

    private Message<byte[]> handleUnauthorizedException(Message<byte[]> clientMessage, Throwable ex) {
        return prepareErrorMessage(ErrorCode.TOKEN_EXPIRED);
    }

    private Message<byte[]> prepareErrorMessage(ErrorCode errorCode) {
        String msg = String.valueOf(errorCode.getMessages());
        StompHeaderAccessor accessor = StompHeaderAccessor.create(StompCommand.ERROR);

        accessor.setMessage(msg);
        accessor.setLeaveMutable(true);

        return MessageBuilder.createMessage(msg.getBytes(StandardCharsets.UTF_8), accessor.getMessageHeaders());
    }


}
