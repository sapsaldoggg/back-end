package login.jwtlogin.auth.email;

import login.jwtlogin.domain.email.ConfirmationToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Slf4j
@Service
public class ConfirmationTokenService {

    private final ConfirmationTokenRepository confirmationTokenRepository;
    private final EmailSenderService emailSenderService;
    public static final String key = createKey();

    @Autowired
    public ConfirmationTokenService(ConfirmationTokenRepository confirmationTokenRepository, EmailSenderService emailSenderService) {
        this.confirmationTokenRepository = confirmationTokenRepository;
        this.emailSenderService = emailSenderService;
    }


    /**
     *
     * @param receiverEmail : 받는 이메일 주소
     * @return : 생성한 토큰
     */
    public void createEmailConfirmationToken(String receiverEmail) {
//        ConfirmationToken confirmationToken = ConfirmationToken.createEmailConfirmationToken();
//        confirmationTokenRepository.save(confirmationToken);

        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(receiverEmail);
        simpleMailMessage.setSubject("[혼밥메이트] 대학교 이메일 인증");
        //simpleMailMessage.setText("링크로 접속하면 인증이 완료됩니다.\nhttps://localhost:8080/confirm-email?token="+confirmationToken.getId()); //생성한 토큰 쿼리파라미터로 전송
        simpleMailMessage.setText("["+createKey()+"]인증번호를 입력해주세요.");
        emailSenderService.sendEmail(simpleMailMessage);
//        return confirmationToken.getId();
    }

    /**
     * 토큰 검증 메소드
     * @param tokenId : 사용자가 링크를 타서 전송된 토큰
     * @return
     */
    public Optional<ConfirmationToken> findExpiredToken(String tokenId) {
        return confirmationTokenRepository.find(tokenId, LocalDateTime.now(), false);
    }

    public static String createKey() {
        StringBuffer key = new StringBuffer();
        Random rnd = new Random();

        for (int i = 0; i < 6; i++) { // 인증코드 6자리
            key.append((rnd.nextInt(10)));
        }
        return key.toString();
    }

}
