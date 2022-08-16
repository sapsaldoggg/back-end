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


    public Optional<Party> findById(Long id) {
        return Optional.ofNullable(em.find(Party.class, id));
    }


    /**
     * @param id
     * @return 파티 정보 반환 (파티 + 멤버 + 식당 + 채팅방)
     */
    public Optional<Party> findWithAllById(Long id) {
        return em.createQuery("select distinct p from Party p " +
                "join fetch p.members " +
                "join fetch p.restaurant " +
                "join fetch p.chatRoom " +
                "where p.id = :id", Party.class)
                .setParameter("id", id)
                .getResultList()
                .stream().findFirst();
    }



    /**
     *
     * @param restaurant_id : 식당 아이디
     * @return 식당에 포함되는 파티 목록 반환 (파티 + 식당)
     */
    public List<Party> findWithRestaurant(Long restaurant_id) {
        return em.createQuery("select p from Party p join fetch p.restaurant r where r.id = :id", Party.class)
                .setParameter("id", restaurant_id)
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




    /**
     * 파티 삭제
     * @param party
     */
    public void removeParty(Party party) {
        em.remove(party);
    }

}
