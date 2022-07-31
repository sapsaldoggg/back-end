package solobob.solobobmate.controller.partyDto;

import solobob.solobobmate.domain.MatchingStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import solobob.solobobmate.domain.Party;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


//파티목록에 필요한 필드만 넣은 dto
@Data
public class PartyDto {

    private Long id;

    private String restaurant;

    private String title;

    private MatchingStatus status;

    private Integer maximumCount;

    private Integer currentCount;

    private LocalDateTime createdAt;

    private List<PartyMembersDto> members;

    public PartyDto(Party party) {
        this.id = party.getId();
        this.restaurant = party.getRestaurant().getName();
        this.title = party.getTitle();
        this.status = party.getMatchingStatus();
        this.maximumCount = party.getMaxNumber();
        this.currentCount = party.getCurrentNumber();
        this.createdAt = party.getCreateAt();
        this.members = party.getMembers().stream().map(m -> new PartyMembersDto(m)).collect(Collectors.toList());
    }
}
