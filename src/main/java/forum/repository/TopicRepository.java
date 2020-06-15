package forum.repository;

import forum.entity.Topic;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface TopicRepository extends CrudRepository<Topic, Long> {

    List<Topic> findBySection_Id(Long id, Pageable pageable);
    Long countBySection_Id(Long id);

    @Query(value = "SELECT u.id AS userId, c.id AS commentId " +
                    "FROM topic t " +
                    "JOIN comment c ON t.id = c.topic_id " +
                    "JOIN \"user\" u ON u.id = c.user_id " +
                    "WHERE t.id = :topicId " +
                    "ORDER BY c.placedat DESC " +
                    "LIMIT 1",
            nativeQuery = true)
    lastPost findLastPost(@Param("topicId") Long topicId);

    static interface lastPost {
        Long getUserId();
        Long getCommentId();
    }
}
