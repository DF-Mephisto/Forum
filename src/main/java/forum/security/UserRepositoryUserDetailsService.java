package forum.security;

import forum.entity.User;
import forum.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserRepositoryUserDetailsService implements UserDetailsService {

    UserRepository UserRepo;

    @Autowired
    UserRepositoryUserDetailsService(UserRepository UserRepo)
    {
        this.UserRepo = UserRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user;

        user = UserRepo.findByUsername(username);

        if (user != null)
        {
            return user;
        }

        throw new UsernameNotFoundException("User " + username + " not found");
    }
}
