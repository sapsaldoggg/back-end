package solobob.solobobmate.controller.memberDto;

import lombok.Getter;

import javax.validation.constraints.NotBlank;


@Getter
public class LoginDto {

    @NotBlank
    private String loginId;

    @NotBlank
    private String password;

}
