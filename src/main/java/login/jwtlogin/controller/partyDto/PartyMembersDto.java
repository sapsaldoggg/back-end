package login.jwtlogin.controller.partyDto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PartyMembersDto {

    private String nickName;

    private String sex;

    private String dept;

    private Integer sno;

    private Long reliability;
}
