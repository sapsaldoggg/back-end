package login.jwtlogin.chat;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@Slf4j
public class ChatController {

    private final SimpMessagingTemplate template;

    @MessageMapping("/party/enter")   // /pub/party/enter
    public void enterChat(){

    }

    @MessageMapping("/party/message")  // /pub/party/message
    public void messageChat() {

    }
}
