package solobob.solobobmate.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import solobob.solobobmate.domain.Member;
import solobob.solobobmate.domain.Report;

import java.util.List;
import java.util.Optional;

public interface ReportRepository extends JpaRepository<Report, Long> {
    List<Report> findAllByToMemberId(Long toMemberId);

    Optional<Report> findByFromMemberAndToMember(Member fromMember, Member toMember);
}
