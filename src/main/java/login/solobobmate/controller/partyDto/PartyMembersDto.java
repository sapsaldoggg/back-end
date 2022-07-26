package login.solobobmate.controller.partyDto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PartyMembersDto {

    private Long id;

    private String nickName;

    private String sex;

    private String dept;

    private Integer sno;

    private Long reliability;

    private Boolean owner;

    private Boolean ready;
}
