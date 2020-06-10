package forum.service;

import forum.entity.User;
import forum.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements ApplicationRunner {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public DataLoader(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void run(ApplicationArguments args) {

        if (userRepository.findByRole("ROLE_ADMIN").isEmpty())
        {
            User admin = new User("admin",
                                  passwordEncoder.encode("admin"),
                             "ROLE_ADMIN");

            userRepository.save(admin);
        }
    }
}
