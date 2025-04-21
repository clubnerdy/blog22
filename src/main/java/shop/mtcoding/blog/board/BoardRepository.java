package shop.mtcoding.blog.board;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class BoardRepository {
    private final EntityManager em;

    public void delete(Board board) {
        em.remove(board);
    }

    public Board findByIdJoinUserReplies(Integer id) {
        Query query = em.createQuery("select b from Board b join fetch b.user LEFT JOIN FETCH b.replies r LEFT JOIN FETCH r.user where b.id = :id ORDER BY r.id DESC", Board.class);
        query.setParameter("id", id);
        return (Board) query.getSingleResult();
    }

    public Board findByIdJoinUser(Integer id) {
        Query query = em.createQuery("select b from Board b join fetch b.user where b.id = :id", Board.class);
        query.setParameter("id", id);
        return (Board) query.getSingleResult();
    }

    public Board findById(Integer id) {
        return em.find(Board.class, id);
    }

    // 페이징

    // 1. 로그인 안했을 때 -> 4개
    // 2.1. 로그인 했을 때 -> ssar -> 5개
    // 2.2. 로그인 했을 때 -> ssar이 아니면 -> 4개
    // 그룹함수
    public Long totalCount() {
        Query query = em.createQuery("select count(b) from Board b where b.isPublic = true", Long.class);
        return (Long) query.getSingleResult();
    }

    public Long totalCount(int userId) {
        Query query = em.createQuery("select count(b) from Board b where b.isPublic = true or b.user.id = :userId", Long.class);
        query.setParameter("userId", userId);
        return (Long) query.getSingleResult();
    }

    // locahost:8080?page=0
    public List<Board> findAll(int page) {
        String sql = "select b from Board b where b.isPublic = true order by b.id desc";
        Query query = em.createQuery(sql, Board.class);
        query.setFirstResult(page * 3);
        query.setMaxResults(3); // 최대 몇개?

        return query.getResultList();
    }

    public List<Board> findAll(Integer userId, int page) {
        String sql = "select b from Board b where b.isPublic = true or b.user.id = :userId order by b.id desc";
        Query query = em.createQuery(sql, Board.class);
        query.setParameter("userId", userId);
        query.setFirstResult(page * 3);
        query.setMaxResults(3);
        return query.getResultList();
    }

//    public List<Board> findAll() {
//        String sql = "select b from Board b where b.isPublic = true order by b.id desc";
//        Query query = em.createQuery(sql, Board.class);
//        return query.getResultList();
//    }
//
//    public List<Board> findAll(Integer userId) {
//        String sql = "select b from Board b where b.isPublic = true or b.user.id = :userId order by b.id desc";
//        Query query = em.createQuery(sql, Board.class);
//        query.setParameter("userId", userId);
//        return query.getResultList();
//    }

    public void save(Board board) {
        em.persist(board);
    }

}
