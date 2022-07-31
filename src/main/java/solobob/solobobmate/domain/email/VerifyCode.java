package solobob.solobobmate.domain.email;

import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@Getter
public class VerifyCode {

    @Id @GeneratedValue
    @Column(name = "verifycode_id")
    private Long id;

    private String code;

    private boolean expired;

    public static VerifyCode createCode(String code) {
        VerifyCode verifyCode = new VerifyCode();
        verifyCode.code = code;
        verifyCode.expired = false;
        return verifyCode;
    }

    public void useCode() {
        this.expired = true;
    }
}
