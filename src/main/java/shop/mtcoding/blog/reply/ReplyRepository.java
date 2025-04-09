package shop.mtcoding.blog.reply;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class ReplyRepository {
    private final EntityManager em;

    public List<Reply> findAllByBoardId(int boardId) {
        Query query = em.createQuery("SELECT r FROM Reply r JOIN FETCH r.user WHERE r.board.id = :boardId", Reply.class);
        query.setParameter("boardId", boardId);
        List<Reply> replies = query.getResultList();
        return replies;
    }
}



