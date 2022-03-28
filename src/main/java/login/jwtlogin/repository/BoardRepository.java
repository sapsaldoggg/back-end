package login.jwtlogin.repository;

import login.jwtlogin.domain.Board;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BoardRepository {

    private final EntityManager em;

    @Transactional
    public void save(Board board) {
        em.persist(board);
    }

    public Board findById(Long id) {
        return em.find(Board.class, id);
    }

    /**
     *
     * @param id : 식당 id
     * @return : 식당에 해당하는 파티목록 반환
     */
    public List<Board> findByRestaurantId(Long id) {
        return em.createQuery("select b from Board b where b.restaurant = :restaurant", Board.class)
                .setParameter("restaurant", id)
                .getResultList();
    }

}
