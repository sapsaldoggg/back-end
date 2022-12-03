package solobob.solobobmate.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import solobob.solobobmate.domain.Chat;

import java.util.List;

public interface ChatRepository extends JpaRepository<Chat, Long> {

    @Query("select c from Chat c join fetch c.chatRoom cr where cr.id = :id")
    List<Chat> findChatsByCreatedDateDesc(@Param("id")Long id, Pageable pageable);
}
