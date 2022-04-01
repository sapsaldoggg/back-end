package login.jwtlogin.controller.memberDTO;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class LoginResultDto {

    private String token;

    private String nickname;

    private String loginId;

    private String sex;

    private String university;

    private String dept;

    private Integer sno;

    private Long reliability;
}
