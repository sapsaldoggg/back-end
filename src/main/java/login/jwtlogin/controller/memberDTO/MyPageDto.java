package login.jwtlogin.controller.memberDTO;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class MyPageDto {

    private String nickname;

    private String sex;

    private String university;

    private String dept;

    private Integer sno;
}
