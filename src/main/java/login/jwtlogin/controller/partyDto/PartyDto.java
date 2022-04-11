package login.jwtlogin.controller.partyDto;

import login.jwtlogin.domain.MatchingStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;


//파티목록에 필요한 필드만 넣은 dto
@Data
@AllArgsConstructor
public class PartyDto {

    private Long id;

    private String owner;  //방장 닉네임

    private String restaurant;

    private String title;

    private LocalDateTime createdTime;

    private MatchingStatus matchingStatus;

    private Integer maxNumber;

    private Integer currentNumber;
}
