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

    private Integer maxNumber;

    private Integer currentNumber;

    public static Party create(Member member, Restaurant restaurant, String title, int maxNumber) {
        Party party = new Party();
        party.owner = member.getNickname();

        //----------member 관련-----------
        party.addMember(member);
        member.updateOwner();
        //-------------------------------

        party.restaurant = restaurant;
        party.title = title;
        party.createdTime = LocalDateTime.now();
        party.matchingStatus = MatchingStatus.NON_MATCHED;
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
    }

    // 매칭상태로 변경
    public void matched() {
        this.matchingStatus = MatchingStatus.MATCHED;
    }




}
