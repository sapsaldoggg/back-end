package login.jwtlogin.repository;

import login.jwtlogin.domain.Chat;
import login.jwtlogin.domain.Party;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ChatRepository {

    private EntityManager em;

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
