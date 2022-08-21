package solobob.solobobmate.domain;

import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
public class Chat extends BaseEntity{

    @Id
    @GeneratedValue
    @Column(name = "chat_id")
    private Long id;


    private String sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chatRoom_id")
    private ChatRoom chatRoom;

    private String message;

    public void addChat(ChatRoom chatRoom){
        chatRoom.getChats().add(this);
        this.chatRoom = chatRoom;
    }

    public static Chat create(ChatRoom chatRoom, String sender, String message) {
        Chat chat = new Chat();
        chat.addChat(chatRoom);
        chat.sender = sender;
        chat.message = message;
        return chat;
    }
}
