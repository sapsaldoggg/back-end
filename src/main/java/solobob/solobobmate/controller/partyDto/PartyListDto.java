package solobob.solobobmate.controller.partyDto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

//파티 목록 반환해주는 dto
@Data
@AllArgsConstructor
public class PartyListDto {

    private Long partyId;

    private List<PartyInfoDto> parties;

    public PartyListDto(List<PartyInfoDto> parties) {
        this.parties = parties;
    }
}
