package login.jwtlogin.repository;


import login.jwtlogin.domain.Party;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PartyRepository {

    private final EntityManager em;

    @Transactional
    public void save(Party party) {
        em.persist(party);
    }

    public List<Party> findAll() {
        return em.createQuery("select p from Party p", Party.class)
                .getResultList();
    }

    public Optional<Party> findById(Long id) {
        return Optional.ofNullable(em.find(Party.class, id));
    }

    /**
     *
     * @param id : 식당 id
     * @return : 식당에 해당하는 파티목록 반환
     */
    public List<Party> findByRestaurantId(Long id) {
        return em.createQuery("select p from Party p where p.restaurant = :restaurant", Party.class)
                .setParameter("restaurant", id)
                .getResultList();
    }

}
