package solobob.solobobmate.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class ChatRoom {

    @Id
    @GeneratedValue
    @Column(name = "chatroom_id")
    private Long id;


    @OneToMany(mappedBy = "chatRoom")
    private List<Chat> chats = new ArrayList<>();

    @OneToOne
    @JoinColumn(name = "party_id")
    private Party party;

    public void setParty(Party party) {
        this.party = party;
    }


}


