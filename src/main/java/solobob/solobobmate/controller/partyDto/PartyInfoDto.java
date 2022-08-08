package solobob.solobobmate.controller.partyDto;

import solobob.solobobmate.domain.MatchingStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import solobob.solobobmate.domain.Party;

import java.time.LocalDateTime;

@Getter
public class PartyInfoDto {

    private Long id;

    private String title;

    private String restaurant;

    private LocalDateTime createdAt;

    private MatchingStatus status;

    private Integer maximumCount;

    private Integer currentCount;

    public PartyInfoDto(Party party) {
        this.id = party.getId();
        this.title = party.getTitle();
        this.restaurant = party.getRestaurant().getName();
        this.createdAt = party.getCreateAt();
        this.status = party.getMatchingStatus();
        this.maximumCount = party.getMaxNumber();;
        this.currentCount = party.getCurrentNumber();
    }
}
