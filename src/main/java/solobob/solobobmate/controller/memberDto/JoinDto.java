package solobob.solobobmate.controller.memberDto;


import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
public class JoinDto {

    @NotBlank
    private String nickname;

    @NotBlank
    private String loginId;

    @NotBlank
    private String password;

    @NotBlank
    private String sex;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String university;

    @NotBlank
    private String dept;

    private Integer sno;   //ex) 16, 17
}
