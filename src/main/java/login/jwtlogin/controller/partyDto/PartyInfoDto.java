package login.jwtlogin.controller.partyDto;

import login.jwtlogin.domain.MatchingStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class PartyInfoDto {

    private Long id;

    private String owner;  //방장 닉네임

    private String restaurant;

    private String title;

    private LocalDateTime createdTime;

    private MatchingStatus matchingStatus;

    private Integer maxNumber;

    private Integer currentNumber;
}
