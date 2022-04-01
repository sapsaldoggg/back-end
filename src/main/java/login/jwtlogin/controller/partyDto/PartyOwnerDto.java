package login.jwtlogin.controller.partyDto;

import login.jwtlogin.domain.Party;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.List;

//파티 목록 반환해주는 dto
@Data
@AllArgsConstructor
public class PartyOwnerDto {

    private Long partyId;

    private List<PartyListDto> parties;

    public PartyOwnerDto(List<PartyListDto> parties) {
        this.parties = parties;
    }
}
