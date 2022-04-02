package login.jwtlogin.repository;

import login.jwtlogin.domain.Chat;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

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

}
