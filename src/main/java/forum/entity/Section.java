package forum.entity;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name="section", schema = "public")
public class Section {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    @NotBlank(message = "Can't be empty")
    @Size(min=1, max=100, message = "Section name must be between 1 and 100 length long")
    String name;

    @Column(name="placedat")
    Date placedAt;

    @OneToMany(mappedBy = "section", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Topic> topics = new ArrayList<>();

    @Transient
    SectionSummary sum = new SectionSummary();

    @Data
    public class SectionSummary {
        String lastPostUsername;
        String lastPostUserrole;
        String lastPostTopicName;
        Long lastPostTopicId;
        Long totalTopics;
        Long totalComments;
    }

    @PrePersist
    public void createdAt()
    {
        this.placedAt = new Date();
    }
}
