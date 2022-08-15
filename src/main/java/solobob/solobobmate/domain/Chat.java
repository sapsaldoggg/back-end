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


    @JoinColumn(name = "member_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chatRoom_id")
    private ChatRoom chatRoom;

    private String message;

    public void addChat(ChatRoom chatRoom){
        chatRoom.getChats().add(this);
        this.chatRoom = chatRoom;
    }

    public static Chat create(ChatRoom chatRoom, Member member, String message) {
        Chat chat = new Chat();
        chat.addChat(chatRoom);
        chat.member = member;
        chat.message = message;
        return chat;
    }
}
