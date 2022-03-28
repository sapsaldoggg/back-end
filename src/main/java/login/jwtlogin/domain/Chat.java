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

    @JoinColumn(name = "board_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Board board;

    @JoinColumn(name = "member_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    private String message;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime sendTime;

    public static Chat create(Board board, Member member, String message) {
        Chat chat = new Chat();
        chat.board = board;
        chat.member = member;
        chat.message = message;
        chat.sendTime = LocalDateTime.now();
        return chat;
    }
}
