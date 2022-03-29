package login.jwtlogin.domain;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
public class Chat {

    @Id
    @GeneratedValue
    @Column(name = "chat_id")
    private Long id;

    @JoinColumn(name = "party_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Party party;

    @JoinColumn(name = "member_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    private String message;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime sendTime;

    public static Chat create(Party party, Member member, String message) {
        Chat chat = new Chat();
        chat.party = party;
        chat.member = member;
        chat.message = message;
        chat.sendTime = LocalDateTime.now();
        return chat;
    }
}
