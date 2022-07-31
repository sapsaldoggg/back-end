package solobob.solobobmate.repository.email;

import solobob.solobobmate.domain.email.VerifyCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.Optional;

@Repository
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class VerifyCodeRepository {

    private final EntityManager em;

    @Transactional
    public void save(VerifyCode verifyCode) {
        em.persist(verifyCode);
    }


    public Optional<VerifyCode> find(String code, Boolean expired) {
        return em.createQuery("select v from VerifyCode v where v.code=:code and v.expired = :expired", VerifyCode.class)
                .setParameter("code", code)
                .setParameter("expired", expired)
                .getResultList()
                .stream()
                .findFirst();
    }
}
