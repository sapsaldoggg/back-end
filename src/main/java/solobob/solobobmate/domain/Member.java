package solobob.solobobmate.domain;

import lombok.*;

import javax.persistence.*;
import java.util.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity{

    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String nickname;

    private String loginId;

    private String password;

    private String sex;

    private String email;

    private String university;

    private String dept;

    private Integer sno;

    private Long reliability;

    private Boolean owner;  //방장여부

    private Boolean isJoined;  //방 참가 여부(보류)

    private Boolean isReady;  //준비 상태 및 시작 여부

    @JoinColumn(name = "party_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Party party;

    private Authority authority;

    //--------------------------------------------------------------------
    public void setParty(Party party) {
        this.party = party;
    }

    public void setIsJoined(Boolean isJoined) {
        this.isJoined = isJoined;
    }

    public void setOwner(Boolean owner) {
        this.owner = owner;
    }

    public void setIsReady(Boolean isReady) {
        this.isReady = isReady;
    }
    //--------------------------------------------------------------------

    @Builder
    public Member(String nickname, String loginId, String password, String sex, String email
            , String university, String dept, Integer sno, Long reliability, Boolean owner
            , Boolean isJoined, Boolean isReady, Authority authority) {
        this.nickname = nickname;
        this.loginId = loginId;
        this.password = password;
        this.sex = sex;
        this.email = email;
        this.university = university;
        this.dept = dept;
        this.sno = sno;
        this.reliability = reliability;
        this.owner = owner;
        this.isJoined = isJoined;
        this.isReady = isReady;
        this.authority = authority;
    }
}
