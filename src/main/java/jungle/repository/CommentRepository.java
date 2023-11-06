package jungle.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jungle.domain.Comment.Comment;
import jungle.domain.Comment.Dto.CommentResponseDto;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Repository
@Transactional
public class CommentRepository {

    @PersistenceContext
    private EntityManager em;

    public void create(Comment comment) {
        em.persist(comment);
    }

    public void delete(Comment comment) {

        em.remove(comment);

    }

    public Comment findOne(Long id) {
        return em.find(Comment.class, id);
    }

    public List<Comment> findAll() {
        return em.createQuery("select i from Comment i", Comment.class).getResultList();
    }

    public List<Comment> findAllOrdered() {
        return em.createQuery("select i from Comment i order by i.createdAt", Comment.class).getResultList();
    }

    public List<CommentResponseDto> findByPostId(Long postId) {
        List<CommentResponseDto> commentResponseDtoList = new ArrayList<>();

        List<Comment> commentList = em.createQuery("select i from Comment i where i.post.post_id =:post_id order by i.createdAt", Comment.class)
                .setParameter("post_id", postId).getResultList();

        for (Comment i : commentList) {
            commentResponseDtoList.add(new CommentResponseDto(i.getCommentId(), i.getPost().getPost_id(),
                    i.getMember().getId(), i.getContent(),i.getLikeCnt(),i.getDislike() ,i.getCreatedAt(), i.getModifiedAt()));
        }

        return commentResponseDtoList;
    }
}
