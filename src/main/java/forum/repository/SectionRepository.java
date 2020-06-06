package forum.repository;

import forum.entity.Section;
import org.springframework.data.repository.CrudRepository;

import org.springframework.data.domain.Pageable;
import java.util.List;

public interface SectionRepository extends CrudRepository<Section, Long> {
    List<Section> findAll(Pageable pageable);
}
