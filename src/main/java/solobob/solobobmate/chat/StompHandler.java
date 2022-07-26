package solobob.solobobmate.chat;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import solobob.solobobmate.auth.PrincipalDetails;
import solobob.solobobmate.domain.Member;
import solobob.solobobmate.jwt.JwtProperties;
import solobob.solobobmate.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class StompHandler implements ChannelInterceptor {

    private final MemberRepository memberRepository;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        if(StompCommand.CONNECT.equals(accessor.getCommand())){
            String jwtToken = accessor.getFirstNativeHeader("Authorization");
            String username = validation(jwtToken);
            if (username != null) {
                Member member = memberRepository.findByLoginId(username).get();
                PrincipalDetails principalDetails = new PrincipalDetails(member);

                Authentication authentication = new UsernamePasswordAuthenticationToken(principalDetails, null, principalDetails.getAuthorities());
                //강제로 세션에 접근하여 authentication 객체 저장(SecurityContextHolder에 저장)
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        return message;
    }

    private String validation(String jwtToken) {
        try{
            return JWT.require(Algorithm.HMAC512(JwtProperties.SECRET)).build().verify(jwtToken).getClaim("loginId")
                    .asString();
        }catch (SignatureException ex) {
            log.error("Invalid JWT signature");
            throw ex;
        } catch (MalformedJwtException ex) {
            log.error("Invalid JWT token");
            throw ex;
        } catch (ExpiredJwtException ex) {
            log.error("Expired JWT token");
            throw ex;
        } catch (UnsupportedJwtException ex) {
            log.error("Unsupported JWT token");
            throw ex;
        } catch (IllegalArgumentException ex) {
            log.error("JWT claims string is empty.");
            throw ex;
        }
    }
}
