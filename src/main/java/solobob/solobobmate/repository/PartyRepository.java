package solobob.solobobmate.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import solobob.solobobmate.domain.Party;

import java.util.List;
import java.util.Optional;

public interface PartyRepository extends JpaRepository<Party, Long> {

    @Query("select p from Party p")
    List<Party> findPartiesByCreatedDateDesc(Pageable pageable);

    @Query("select p from Party p where p.title = :keyword")
    List<Party> findWithKeyword(@Param("keyword")String keyword, Pageable pageable);

    @EntityGraph(attributePaths = {"members","restaurant","chatRoom"})
    @Query("select p from Party p where p.id = :id")
    Optional<Party> findWithAllById(@Param("id")Long id);

    // @EntityGraph(default:left join fetch) 이므로 일관성을 깸
    @Query("select p from Party p join fetch p.restaurant r where r.id = :id")
    List<Party> findWithRestaurant(@Param("id") Long id);

}
