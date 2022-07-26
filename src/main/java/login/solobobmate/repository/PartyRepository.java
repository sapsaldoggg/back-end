package login.solobobmate.repository;


import login.solobobmate.domain.Member;
import login.solobobmate.domain.Party;
import login.solobobmate.domain.Restaurant;
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
     * @param id : 식당 객체
     * @return : 식당에 해당하는 파티목록 반환
     */
    public List<Party> findByRestaurantId(Restaurant restaurant) {
        return em.createQuery("select p from Party p where p.restaurant = :restaurant", Party.class)
                .setParameter("restaurant", restaurant)
                .getResultList();
    }

    /**
     *
     * @param : member 객체
     * @return : 요청한 회원이 방장을 맡고 있는 파티가 있을 시 해당 파티 반환
     */
    public Optional<Party> findPartyOwnerByMemberId(Member member) {
        return em.createQuery("select p from Party p where p.member = :member", Party.class)
                .setParameter("member", member)
                .getResultList()
                .stream()
                .findFirst();
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
