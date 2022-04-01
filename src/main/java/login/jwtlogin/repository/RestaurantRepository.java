package login.jwtlogin.repository;

import login.jwtlogin.domain.Restaurant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RestaurantRepository {

    private final EntityManager em;

    @Transactional
    public void save(Restaurant restaurant) {
        em.persist(restaurant);
    }

    public Optional<Restaurant> findById(Long id) {
        return Optional.ofNullable(em.find(Restaurant.class, id));
    }

    public List<Restaurant> findAll() {
        return em.createQuery("select r from Restaurant r", Restaurant.class)
                .getResultList();
    }



}
