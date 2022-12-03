package solobob.solobobmate.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import solobob.solobobmate.domain.Member;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {


    Optional<Member> findByLoginId(String loginId);

    Optional<Member> findByNickname(String nickname);

    @EntityGraph(attributePaths = {"party", "party.chatRoom", "party.restaurant", "party.members"})
    @Query("select m from Member m where m.loginId = :loginId")
    Optional<Member> findByLoginIdWithParty(@Param("loginId") String loginId);
}
