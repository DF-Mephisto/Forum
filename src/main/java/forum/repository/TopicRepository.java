package forum.repository;

import forum.entity.Topic;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface TopicRepository extends CrudRepository<Topic, Long> {

    List<Topic> findBySection_Id(Long id, Pageable pageable);
}
