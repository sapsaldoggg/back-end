package login.jwtlogin.controller.partyDto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data  //입력받는 dto는 기본 생성자가 존재해야함!! (결과로 반환하는 dto는 상관없음)
public class PartyUpdateDto {

    private String title;

    private Integer maxNumber;

}
