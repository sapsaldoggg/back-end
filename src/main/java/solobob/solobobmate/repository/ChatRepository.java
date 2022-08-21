package solobob.solobobmate.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import solobob.solobobmate.domain.Chat;

public interface ChatRepository extends JpaRepository<Chat, Long> {
}
