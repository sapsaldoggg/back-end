package login.jwtlogin.domain;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
public class Party {

    @Id
    @GeneratedValue
    @Column(name = "party_id")
    private Long id;

    @JoinColumn(name = "member_id")
    @ManyToOne
    private Member member;

    @JoinColumn(name = "restaurant_id")
    @OneToOne
    private Restaurant restaurant;

    private String title;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdTime;

    @Enumerated(EnumType.STRING)
    private MatchingStatus matchingStatus;

    private Integer maxNumber;

    private Integer currentNumber;

    public static Party create(Member member, Restaurant restaurant, String title, int maxNumber) {
        Party party = new Party();
        party.member = member;
        party.restaurant = restaurant;
        party.title = title;
        party.createdTime = LocalDateTime.now();
        party.matchingStatus = MatchingStatus.NON_MATCHED;
        party.maxNumber = maxNumber;
        party.currentNumber = 0;
        return party;
    }

    public void update(String title, int maxNumber) {
        this.title = title;
        this.maxNumber = maxNumber;
    }




}
