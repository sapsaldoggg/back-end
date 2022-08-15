package solobob.solobobmate.domain;

import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Party extends BaseEntity{

    @Id
    @GeneratedValue
    @Column(name = "party_id")
    private Long id;

    //---------------------------------------------------------------------
    @OneToMany(mappedBy = "party")
    private List<Member> members = new ArrayList<>();


    @OneToOne(mappedBy = "party", fetch = FetchType.LAZY)
    private ChatRoom chatRoom;

    //---------------------------------------------------------------------

    @JoinColumn(name = "restaurant_id")
    @OneToOne(fetch = FetchType.LAZY)
    private Restaurant restaurant;

    private String title;

    //방장 닉네임
    private String owner;

    @Enumerated(EnumType.STRING)
    private MatchingStatus matchingStatus;

    @Enumerated(EnumType.STRING)
    private FullStatus fullStatus;

    private Integer maxNumber;

    private Integer currentNumber;

    // 매칭됬을때, 시간
    private LocalDateTime matchingStartTime;

    public void initialOwner(Member member) {
        member.setParty(this);
        member.setIsJoined(true);
        member.setOwner(true);
        this.members.add(member);
        this.owner = member.getNickname();
    }

    public void initialChatRoom(ChatRoom chatRoom) {
        this.chatRoom = chatRoom;
        chatRoom.setParty(this);
    }

    public static Party create(Member member, Restaurant restaurant, ChatRoom chatRoom, String title, int maxNumber) {
        Party party = new Party();

        party.initialOwner(member);
        party.initialChatRoom(chatRoom);

        party.restaurant = restaurant;
        party.title = title;
        party.matchingStatus = MatchingStatus.NON_MATCHED;
        party.fullStatus = FullStatus.NON_FULL;
        party.maxNumber = maxNumber;
        party.currentNumber = 1; //방장 포함
        return party;
    }

    // 파티 수정
    public Party update(String title, int maxNumber) {
        this.title = title;
        this.maxNumber = maxNumber;
        return this;
    }

    //파티에 멤버 추가
    public void addMember(Member member) {
        this.members.add(member);
        member.setParty(this);
        member.setIsJoined(true);
        this.currentNumber++;  //현재인원수 증가
        if (this.currentNumber == this.maxNumber) {
            full(); //full 로 변경
        }
    }

    //인원 다참 상태 변경
    public void full() {
        this.fullStatus = FullStatus.FULL;
    }

    public void nonFull() {
        this.fullStatus = FullStatus.NON_FULL;
    }

    // 멤버 삭제 (방장제외)
    public void deleteMember(Member member) {
        member.setParty(null);
        member.setIsJoined(false);
        this.getMembers().remove(member);
        this.currentNumber--;
        nonFull();
    }

    //파티 시작취소
    public void startCancelParty(Member member) {
        member.setIsReady(false);
        this.matchingStatus = MatchingStatus.NON_MATCHED;
        this.matchingStartTime = null;
    }

    //파티 시작
    public void startParty(Member member) {
        member.setIsReady(true);
        this.matchingStatus = MatchingStatus.MATCHED;
        this.matchingStartTime = LocalDateTime.now();
    }

    //준비완료
    public void ready(Member member) {
        member.setIsReady(true);
    }

    //준비취소
    public void cancelReady(Member member) {
        member.setIsReady(false);
    }



}
