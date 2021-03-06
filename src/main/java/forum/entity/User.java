package forum.entity;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

@Data
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name="user", schema = "public")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    @NotBlank(message = "Can't be empty")
    @Size(min=1, max=20, message = "Name must be between 1 and 20 length long")
    String username;

    @NotBlank(message = "Can't be empty")
    @Size(min=4, max=20, message = "Password must be between 4 and 20 length long")
    String password;

    String role;

    @Size(max=1000, message = "Information must be not bigger than 1000 length long")
    String information = "";

    @Column(name="registrationdate")
    Date registrationDate;
    byte[] avatar;

    public User()
    {
        role = "ROLE_USER";
    }

    public User(String username, String password, String role)
    {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public String getImageStr()
    {
        if (avatar == null) return "";
        else return "data:image;base64," + new String(avatar);
    }

    public String getRegDate()
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(registrationDate);
    }

    @PrePersist
    public void registeredAt()
    {
        this.registrationDate = new Date();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities()
    {
        return Arrays.asList(new SimpleGrantedAuthority(this.role));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
