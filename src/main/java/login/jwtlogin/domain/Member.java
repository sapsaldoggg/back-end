package login.jwtlogin.domain;

import lombok.*;

import javax.persistence.*;
import java.awt.datatransfer.Clipboard;
import java.util.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Member {

    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String nickname;

    private String loginId;

    private String password;

    private String sex;

    private String roles;

    private String email;

    private String university;

    private String dept;

    private Integer sno;

    private Long reliability;

    private Boolean owner;  //방장여부

    private Boolean isJoined;  //방 참가 여부(보류)

    private Boolean isReady;  //준비 상태 및 시작 여부

    @JoinColumn(name = "party_id")
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Party party;

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

//    @ElementCollection
//    @CollectionTable(name = "friend" , joinColumns =
//        @JoinColumn(name = "member_id")
//    )
//    private Set<Long> friends = new HashSet<>();
//
//
//    @ElementCollection
//    @CollectionTable(name = "favorite_food" , joinColumns =
//    @JoinColumn(name = "member_id")
//    )
//    private List<String> favoriteFoods = new ArrayList<>();


//    @OneToMany(mappedBy = "member")
//    private List<Party> parties = new ArrayList<>();

    //------------------------------------------------------------------------------------------------


//    public Member(String nickname, String loginId, String password, String sex, String email, String roles, String university, String dept, Integer sno, Long reliability) {
//        this.nickname = nickname;
//        this.loginId = loginId;
//        this.password = password;
//        this.sex = sex;
//        this.email = email;
//        this.roles= roles;
//        this.university = university;
//        this.dept = dept;
//        this.sno = sno;
//        this.reliability = reliability;
//    }

    public List<String> getRoleList() {
        if (this.roles.length() > 0) {
            return Arrays.asList(this.roles.split(","));
        }else{
            return new ArrayList<>();
        }
    }





}
