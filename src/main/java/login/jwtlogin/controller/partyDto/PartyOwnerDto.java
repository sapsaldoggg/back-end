package login.jwtlogin.controller.partyDto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

//파티 목록 반환해주는 dto
@Data
@AllArgsConstructor
public class PartyOwnerDto {

    private Long partyId;

    private List<PartyDto> parties;

    public PartyOwnerDto(List<PartyDto> parties) {
        this.parties = parties;
    }
}
