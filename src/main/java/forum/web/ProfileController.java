package forum.web;

import forum.entity.User;
import forum.forms.EditProfileForm;
import forum.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Optional;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    private UserRepository userRepo;
    private PasswordEncoder encoder;

    @Autowired
    public ProfileController(UserRepository userRepo, PasswordEncoder encoder)
    {
        this.userRepo = userRepo;
        this.encoder = encoder;
    }

    @ModelAttribute(name="userImg")
    public String userImg(@AuthenticationPrincipal User user)
    {
        return user == null ? "" : user.getImageStr();
    }

    @GetMapping("/{userName}")
    public ModelAndView getUserPage(@PathVariable("userName") String userName)
    {
        ModelAndView mav = new ModelAndView("userPage");

        User user = userRepo.findByUsername(userName);
        if (user == null) {
            mav.setViewName("redirect:/");
            return mav;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String regDate = sdf.format(user.getRegistrationDate());

        mav.addObject("regDate", regDate);
        mav.addObject("user", user);

        return mav;
    }

    @GetMapping("/edit")
    public String getUserEditPage(@AuthenticationPrincipal User user,
                                  Model model)
    {
        EditProfileForm form = new EditProfileForm();
        form.setInformation(user.getInformation());
        model.addAttribute("editForm", form);

        return "editProfile";
    }

    @PostMapping("/edit")
    public String processProfileEditing(@Valid @ModelAttribute("editForm") EditProfileForm form,
                                        BindingResult errors,
                                        @RequestParam("avatarImg") MultipartFile avatarImg,
                                        @AuthenticationPrincipal User authUser)
    {
        Optional<User> res = userRepo.findById(authUser.getId());
        if (res.isEmpty()) return "redirect:/";
        User user = res.get();
        String redirUrl = "/profile/" + user.getUsername();

        try {
            if (!encoder.matches(form.getOldPassword(), user.getPassword()))
            {
                throw new IOException("Wrong password");
            }

        } catch (IOException e)
        {
            errors.rejectValue("oldPassword", "error.form", "Wrong password");
        }

        if (errors.hasErrors())
        {
            return "editProfile";
        }

        if (!avatarImg.isEmpty())
        {
            try {
                user.setAvatar(Base64.getEncoder().encode(avatarImg.getBytes()));
            } catch (IOException e) {
                errors.rejectValue("avatarImg", "error.form", "Error during file uploading has occurred");
                return "editProfile";
            }
        }

        user.setPassword(encoder.encode(form.getPassword()));
        user.setInformation((form.getInformation()));
        userRepo.save(user);

        Authentication authentication = new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return "redirect:" + redirUrl;
    }
}
