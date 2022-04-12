package login.jwtlogin.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import login.jwtlogin.auth.PrincipalDetails;
import login.jwtlogin.controller.memberDTO.LoginDto;
import login.jwtlogin.controller.memberDTO.LoginResultDto;
import login.jwtlogin.domain.Member;
import login.jwtlogin.result.ErrorResult;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

/**
 * 스프링 security에서 /login post 요청으로 username, password 전송하면==>
 * ==> UsernamePasswordAuthenticationFilter가 동작함
 */
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    /**
     * 실행 순서
     * 1) attemptAuthentication 정상 처리 되면
     * 2) successfulAuthentication 실행
     */

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        log.info("로그인 시도중");

        /**
         * 1) username, password 받아서 정상인지 로그인 시도
         * 2) authenticationManager 로 로그인 시도를 하면 ==> PrincipalDetailsService가 호출
         * 3) PrincipalDetails 를 세션(securityContextHolder)에 담고 (권한 관리를 위해서)
         * 4) jwt 토큰을 만들어서 응답
         */

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            LoginDto loginDto = objectMapper.readValue(request.getInputStream(), LoginDto.class);
            log.info("memberDto {}", loginDto.toString());
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(loginDto.getLoginId(), loginDto.getPassword());

            Authentication authentication = authenticationManager.authenticate(authenticationToken);

            PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
            log.info("principalDetail {}", principalDetails.getUsername());
            //username이 출력됨 => 로그인 정상 처리

            //권한 관리를 spring security에 위임하기 위해 리턴
            return authentication;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * successfulAuthentication
     * => jwt토큰을 생성해서 response 해주는 역할
     */

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        log.info("successful");

        PrincipalDetails principalDetails = (PrincipalDetails) authResult.getPrincipal();
        Member member = principalDetails.getMember();

        //토큰 생성
        String jwtToken = JWT.create()
                .withSubject(principalDetails.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis()+JwtProperties.EXPIRATION_TIME))  //10분
                .withClaim("id", principalDetails.getMember().getId())
                .withClaim("loginId", principalDetails.getMember().getLoginId())
                .sign(Algorithm.HMAC512(JwtProperties.SECRET));

        response.addHeader(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX+jwtToken);

//        ObjectMapper objectMapper = new ObjectMapper();
////        LoginResultDto loginResultDto = LoginResultDto.builder()
////                .token(jwtToken)
////                .nickname(member.getNickname())
////                .loginId(member.getLoginId())
////                .sex(member.getSex())
////                .university(member.getUniversity())
////                .dept(member.getDept())
////                .sno(member.getSno())
////                .reliability(member.getReliability())
////                .build();
//
//        String jsonToken = objectMapper.writeValueAsString(new Token(jwtToken));
//        response.setContentType("application/json");
//        response.setCharacterEncoding("utf-8");
//        response.getWriter().write(jsonToken);
//        //body 로 전송하는것으로 수정 ( 토큰 값만 )
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        ObjectMapper objectMapper = new ObjectMapper();

        String error_json = objectMapper.writeValueAsString(new ErrorResult("LOGIN_FAIL", "로그인에 실패했습니다"));
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.setStatus(400);
        response.getWriter().write(error_json);
    }

    @Getter
    @AllArgsConstructor
    static class Token{
        private String token;
    }






}
