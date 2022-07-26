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

    @JoinColumn(name = "party_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Party party;

    @JoinColumn(name = "member_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    private String message;

    private LocalDateTime sendTime;

    public static Chat create(Party party, Member member, String message) {
        Chat chat = new Chat();
        chat.party = party;
        chat.member = member;
        chat.message = message;
        return chat;
    }
}
