package forum.forms;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EditProfileForm {

    @NotBlank(message = "Can't be empty")
    @Size(min=6, max=20, message = "Password must be between 6 and 20 length long")
    String password;

    String oldPassword;

    @Size(max=1000, message = "Information must be not bigger than 1000 length long")
    String information = "";

    byte[] avatar;
}
