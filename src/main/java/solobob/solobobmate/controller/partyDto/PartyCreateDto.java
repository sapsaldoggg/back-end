package solobob.solobobmate.controller.partyDto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;

@Getter  //입력받는 dto는 기본 생성자가 존재해야함!! (결과로 반환하는 dto는 상관없음)
@NoArgsConstructor
public class PartyCreateDto {

    @NotBlank
    private String title;

    @Max(value = 8, message = "최대 8명까지만 가능합니다")
    private Integer maximumCount;

}
