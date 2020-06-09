package forum.web;

import forum.entity.User;
import forum.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Base64;

@Controller
@RequestMapping("/register")
public class RegistrationController {

    private UserRepository UserRepo;
    private PasswordEncoder encoder;

    @Autowired
    public RegistrationController(UserRepository UserRepo, PasswordEncoder encoder)
    {
        this.UserRepo = UserRepo;
        this.encoder = encoder;
    }

    @GetMapping
    public String registerForm(Model model)
    {
        model.addAttribute("user", new User());

        return "register";
    }

    @PostMapping
    public String processRegistration(@Valid @ModelAttribute("user") User user,
                                      BindingResult errors,
                                      @RequestParam("avatarImg") MultipartFile avatarImg)
    {
        try {
            User checkUser = UserRepo.findByUsername(user.getUsername());

            if (checkUser != null) throw new IOException("User already exists");

        } catch (IOException e)
        {
            errors.rejectValue("username", "error.regForm", "User with the same name already exists");
        }

        if (errors.hasErrors())
        {
            return "register";
        }

        if (!avatarImg.isEmpty())
        {
            try {
                user.setAvatar(Base64.getEncoder().encode(avatarImg.getBytes()));
            } catch (IOException e) {
                errors.rejectValue("avatarImg", "error.user", "Error during file uploading has occurred");
                return "register";
            }
        }

        user.setPassword(encoder.encode(user.getPassword()));
        UserRepo.save(user);
        return "redirect:/login";
    }
}
