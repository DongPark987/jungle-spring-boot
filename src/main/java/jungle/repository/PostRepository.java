package jungle.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jungle.Domain.Post.Post;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public class PostRepository {
    @PersistenceContext
    private EntityManager em;

    public void create(Post post) {
        em.persist(post);
    }

    public void delete(Post post) {

        em.flush();

    }

    public Post findOne(Long id) {
        return em.find(Post.class, id);
    }

    public List<Post> findAll() {
        return em.createQuery("select i from Post i", Post.class).getResultList();
    }

    public List<Post> findAllOrdered() {
        return em.createQuery("select i from Post i order by i.postDate", Post.class).getResultList();
    }


//    public List<Member> findByName(String name){
//        return em.createQuery("select m from Member m where m.name =:name", Member.class)
//                .setParameter("name",name)
//                .getResultList();
//    }
}
