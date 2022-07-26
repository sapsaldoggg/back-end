package login.solobobmate.controller.partyDto;

import login.solobobmate.domain.MatchingStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;


//파티목록에 필요한 필드만 넣은 dto
@Data
@AllArgsConstructor
public class PartyDto {

    private Long id;

    private String restaurant;

    private String title;

    private MatchingStatus status;

    private Integer maximumCount;

    private Integer currentCount;

    private LocalDateTime createdAt;

    private List<PartyMembersDto> members;
}
