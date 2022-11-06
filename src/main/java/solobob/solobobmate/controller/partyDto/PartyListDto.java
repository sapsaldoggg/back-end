package solobob.solobobmate.controller.partyDto;

import lombok.Data;
import solobob.solobobmate.domain.Party;

import java.util.List;
import java.util.stream.Collectors;

//파티 목록 반환해주는 dto
@Data
public class PartyListDto {

    private List<PartyInfoDto> parties;


    public PartyListDto(List<Party> parties){
        this.parties = parties.stream().map(p -> new PartyInfoDto(p)).collect(Collectors.toList());
    }
}
