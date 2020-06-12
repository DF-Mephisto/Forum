package forum.repository;

import forum.entity.Comment;
import org.springframework.data.repository.CrudRepository;

public interface CommentRepository extends CrudRepository<Comment, Long> {

}
