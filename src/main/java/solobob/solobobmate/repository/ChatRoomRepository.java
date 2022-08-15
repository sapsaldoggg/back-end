package solobob.solobobmate.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import solobob.solobobmate.domain.ChatRoom;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
}
