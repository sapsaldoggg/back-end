package solobob.solobobmate.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import solobob.solobobmate.domain.Chat;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChatRepository {

    private final EntityManager em;

    public void save(Chat chat){
        em.persist(chat);
    }

    public Optional<Chat> findById(Long id){
        return Optional.ofNullable(em.find(Chat.class, id));
    }

    public List<Chat> findChatsByCreatedDateDesc(Long chatRoomId, int offset){
        String query = "select c from Chat c join fetch c.chatRoom cr where cr.id = :chatRoomId order by c.createAt desc";
        return em.createQuery(query, Chat.class)
                .setParameter("chatRoomId", chatRoomId)
                .setFirstResult(offset)
                .setMaxResults(50)
                .getResultList();

    }
}
