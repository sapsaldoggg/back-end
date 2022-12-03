package solobob.solobobmate.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import solobob.solobobmate.domain.Restaurant;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

}
