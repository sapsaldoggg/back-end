package login.solobobmate.repository;

import login.solobobmate.domain.Chat;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

@Transactional(readOnly = true)
@Repository
@RequiredArgsConstructor
public class ChatRepository {

    private EntityManager em;

    @Transactional
    public void save(Chat chat) {
        em.persist(chat);
    }

    public Chat findById(Long id) {
        return em.find(Chat.class, id);
    }

    public List<Chat> findAll() {
        return em.createQuery("select c from Chat c", Chat.class)
                .getResultList();
    }

}
