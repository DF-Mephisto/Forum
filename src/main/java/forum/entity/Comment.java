package forum.entity;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.text.SimpleDateFormat;
import java.util.Date;

@Data
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name="comment", schema = "public")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @NotBlank(message = "Can't be empty")
    @Size(min=1, max=1000, message = "Comment must be between 1 and 1000 length long")
    String text;

    @Column(name="placedat")
    Date placedAt;

    @ManyToOne
    @JoinColumn(name = "topic_id")
    Topic topic;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;

    public String getDate()
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return sdf.format(placedAt);
    }

    @PrePersist
    public void postedAt()
    {
        this.placedAt = new Date();
    }
}
