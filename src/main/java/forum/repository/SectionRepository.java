package forum.repository;

import forum.entity.Section;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SectionRepository extends CrudRepository<Section, Long> {
    List<Section> findAll(Pageable pageable);

    @Query(value = "SELECT res.username, res.userrole, res.topicname, res.topicid " +
                    "FROM ( " +
                        "SELECT c.text, u.username, u.role AS userrole, t.id AS topicid, t.name AS topicname, s.id, c.placedat, " +
                        "row_number() over (partition by s.id order by c.placedat desc) AS p " +
                        "FROM section s " +
                        "JOIN topic t ON s.id = t.section_id " +
                        "JOIN comment c ON t.id = c.topic_id " +
                        "JOIN \"user\" u ON u.id = c.user_id " +
                    ") AS res " +
                    "WHERE res.p = 1 AND res.id = :sectionId",
            nativeQuery = true)
    lastPost findLastPost(@Param("sectionId") Long sectionId);

    @Query(value = "SELECT COUNT(*)\n" +
            "FROM section s\n" +
            "JOIN topic t ON s.id = t.section_id\n" +
            "WHERE s.id = :sectionId",
            nativeQuery = true)
    Long countTopics(@Param("sectionId") Long sectionId);

    @Query(value = "SELECT COUNT(*)\n" +
            "FROM section s\n" +
            "JOIN topic t ON s.id = t.section_id\n" +
            "JOIN comment c ON t.id = c.topic_id\n" +
            "WHERE s.id = :sectionId",
            nativeQuery = true)
    Long countComments(@Param("sectionId") Long sectionId);

    static interface lastPost {
        String getUsername();
        String getUserRole();
        String getTopicName();
        Long getTopicid();
    }

}
