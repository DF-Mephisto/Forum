package forum.entity;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name="section")
public class Section {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    @NotBlank
    @Size(min=1, max=100, message = "Comment must be between 1 and 100 length long")
    String name;

}
