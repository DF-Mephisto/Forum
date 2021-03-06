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
@Table(name="topic", schema = "public")
public class Topic {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    @NotBlank(message = "Can't be empty")
    @Size(min=1, max=100, message = "Topic name must be between 1 and 100 length long")
    String name;

    @Column(name="placedat")
    Date placedAt;

    @ManyToOne
    @JoinColumn(name = "section_id")
    Section section;

    @OneToMany(mappedBy = "topic", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Comment> comments = new ArrayList<>();

    Long views;

    @Transient
    TopicSummary sum = new TopicSummary();

    @Data
    public class TopicSummary {
        User lastPostUser;
        Comment lastPostComment;
        Long totalPosts;
    }

    @PrePersist
    public void createdAt()
    {
        this.placedAt = new Date();
    }
}
