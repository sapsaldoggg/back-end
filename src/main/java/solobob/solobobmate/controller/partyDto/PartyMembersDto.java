package solobob.solobobmate.controller.partyDto;

import lombok.Data;
import solobob.solobobmate.domain.Member;

@Data
public class PartyMembersDto {

    private Long id;

    private String nickname;

    private String sex;

    private String dept;

    private Integer sno;

    private Long reliability;

    private Boolean owner;

    private Boolean ready;

    public PartyMembersDto(Member member) {
        this.id = member.getId();
        this.nickname = member.getNickname();
        this.sex = member.getSex();
        this.dept = member.getDept();
        this.sno = member.getSno();
        this.reliability = member.getReliability();
        this.owner = member.getOwner();
        this.ready = member.getIsReady();
    }
}
