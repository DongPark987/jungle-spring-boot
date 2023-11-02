package jungle.service;

import jungle.Domain.Post.Post;
import jungle.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
//@Transactional(readOnly = true)
@Transactional
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;

    public void create(Post post){
        postRepository.create(post);
    }

    public void delete(Post post){
        postRepository.delete(post);
    }
    public List<Post> findAll(){
        return postRepository.findAll();
    }

    public List<Post> findAllOrdered(){
        return postRepository.findAllOrdered();
    }

    public Post findById(Long id){
        return postRepository.findOne(id);
    }


}
