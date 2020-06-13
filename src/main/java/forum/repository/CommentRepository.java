package forum.repository;

import forum.entity.Comment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CommentRepository extends CrudRepository<Comment, Long> {
    List<Comment> findByTopic_Id(Long id, Pageable pageable);
    Long countByTopic_Id(Long id);
}
