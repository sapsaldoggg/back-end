package solobob.solobobmate.repository;

import solobob.solobobmate.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
@Transactional(readOnly = true)
public class MemberRepository{

    private final EntityManager em;

    public void save(Member member) {
        em.persist(member);
    }

    public Optional<Member> findById(Long id) {
        return Optional.ofNullable(em.find(Member.class, id));
    }

    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }

    public Optional<Member> findByLoginId(String loginId) {
        return em.createQuery("select m from Member m where m.loginId= :loginId", Member.class)
                .setParameter("loginId", loginId)
                .getResultList()
                .stream()
                .findFirst();
    }

    public Optional<Member> findByNickName(String nickname) {
        return em.createQuery("select m from Member m where m.nickname = :nickname", Member.class)
                .setParameter("nickname", nickname)
                .getResultList()
                .stream().findFirst();
    }

    //파티 삭제
    //@Transactional
    public void remove(Member member) {
        em.remove(member);
    }


    //내 파티 정보 조회
    public Optional<Member> findByLoginIdWithParty(String loginId) {
        return em.createQuery("select m from Member m join fetch m.party p join fetch p.restaurant where m.loginId = :loginId", Member.class)
                .setParameter("loginId", loginId)
                .getResultList()
                .stream().findFirst();
    }



}
