package forum.forms;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateTopicForm {

    @NotBlank(message = "Can't be empty")
    @Size(min=1, max=100, message = "Topic name must be between 1 and 100 length long")
    String name;

    @NotBlank(message = "Can't be empty")
    @Size(min=1, max=1000, message = "Comment must be between 1 and 1000 length long")
    String text;
}
