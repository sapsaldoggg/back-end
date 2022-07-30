package solobob.solobobmate.repository;


import solobob.solobobmate.domain.Member;
import solobob.solobobmate.domain.Party;
import solobob.solobobmate.domain.Restaurant;
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
     * @param restaurant : 식당 객체
     * @return : 식당에 해당하는 파티목록 반환
     */
    public List<Party> findByRestaurantId(Restaurant restaurant) {
        return em.createQuery("select p from Party p where p.restaurant = :restaurant", Party.class)
                .setParameter("restaurant", restaurant)
                .getResultList();
    }



    /**
     *
     * @param ownerName : 방장 이름
     * @return  방장 이름에 해당하는 파티 있을 시, 반환
     */

    public Optional<Party> findByOwnerNickName(String ownerName) {
        return em.createQuery("select p from Party p where p.owner = :owner", Party.class)
                .setParameter("owner", ownerName)
                .getResultList()
                .stream().findFirst();
    }




    //파티 삭제
    public void removeParty(Party party) {
        em.remove(party);
    }

}
