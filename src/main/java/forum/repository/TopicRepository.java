package forum.repository;

import forum.entity.Topic;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface TopicRepository extends CrudRepository<Topic, Long> {

    @Query(value = "SELECT res.id, res.name, res.section_id, res.placedat, res.views "+
                    "FROM ( "+
                        "SELECT t.id, t.name, t.section_id, t.placedat, t.views, c.placedat AS lastcommentdate, "+
                        "row_number() over (partition by t.id order by c.placedat desc) AS num "+
                        "FROM topic t "+
                        "JOIN comment c ON t.id = c.topic_id "+
                        "WHERE t.section_id = :sectionId "+
                    ") AS res "+
                    "WHERE res.num = 1 "+
                    "ORDER BY res.lastcommentdate DESC ",
            countQuery = "SELECT COUNT(*) " +
                            "FROM topic t " +
                            "WHERE t.section_id = :sectionId ",
            nativeQuery = true)
    Page<Topic> findBySection_Id(@Param("sectionId") Long sectionId, Pageable pageable);

    Long countBySection_Id(Long id);
    List<Topic> findByNameIgnoreCaseContaining(String name, Pageable pageable);
    Long countByNameIgnoreCaseContaining(String name);

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
