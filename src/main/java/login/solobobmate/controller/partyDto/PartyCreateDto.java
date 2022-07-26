package login.solobobmate.controller.partyDto;

import lombok.Data;

import javax.validation.constraints.Max;

@Data  //입력받는 dto는 기본 생성자가 존재해야함!! (결과로 반환하는 dto는 상관없음)
public class PartyCreateDto {

    private String title;

    @Max(value = 8, message = "최대 8명까지만 가능합니다")
    private Integer maximumCount;

}
