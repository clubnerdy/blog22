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

    public Reply findById(Integer id) {
        return em.find(Reply.class, id);
    } // 얘를 타야 인덱스를 타고 캐싱도 함 프라이머리키라서

    public List<Reply> findAllByBoardId(int boardId) {
        Query query = em.createQuery("SELECT r FROM Reply r JOIN FETCH r.user WHERE r.board.id = :boardId", Reply.class);
        query.setParameter("boardId", boardId);
        List<Reply> replies = query.getResultList();
        return replies;
    }

    public Reply save(Reply reply) {
        em.persist(reply);
        return reply;
    }

    public void deleteById(Integer id) {
        em.createQuery("delete from Reply r where r.id = :id")
                .setParameter("id", id)
                .executeUpdate();
    }
}



