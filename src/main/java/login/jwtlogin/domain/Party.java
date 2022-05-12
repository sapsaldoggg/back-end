package login.jwtlogin.domain;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Party {

    @Id
    @GeneratedValue
    @Column(name = "party_id")
    private Long id;

    //---------------------------------------------------------------------
    @OneToMany(mappedBy = "party")
    private List<Member> members = new ArrayList<>();
    //---------------------------------------------------------------------

    @JoinColumn(name = "restaurant_id")
    @OneToOne(fetch = FetchType.LAZY)
    private Restaurant restaurant;

    private String title;

    //방장 닉네임
    private String owner;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdTime;

    @Enumerated(EnumType.STRING)
    private MatchingStatus matchingStatus;

    @Enumerated(EnumType.STRING)
    private FullStatus fullStatus;

    private Integer maxNumber;

    private Integer currentNumber;

    public static Party create(Member member, Restaurant restaurant, String title, int maxNumber) {
        Party party = new Party();
        party.owner = member.getNickname();

        //----------member 관련-----------
        party.members.add(member);
        member.setParty(party);
        member.updateOwner();
        member.setIsJoined(true);
        //-------------------------------

        party.restaurant = restaurant;
        party.title = title;
        party.createdTime = LocalDateTime.now();
        party.matchingStatus = MatchingStatus.NON_MATCHED;
        party.fullStatus = FullStatus.NON_FULL;
        party.maxNumber = maxNumber;
        party.currentNumber = 1; //방장 포함
        return party;
    }

    // 이거는 뭐였지??
    public void update(String title, int maxNumber) {
        this.title = title;
        this.maxNumber = maxNumber;
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

    // 매칭상태로 변경(방장이 시작 눌렀을때)
    public void matched() {
        this.matchingStatus = MatchingStatus.MATCHED;
    }

    //인원 다참 상태 변경
    public void full() {
        this.fullStatus = FullStatus.FULL;
    }

    // 멤버 삭제
    public void deleteMember(Member member) {
        member.setParty(null);
        member.setIsJoined(false);
        this.getMembers().remove(member);
        this.currentNumber--;
    }






}
