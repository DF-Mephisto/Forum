package forum.web;

import forum.entity.User;
import forum.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    private UserRepository UserRepo;

    @Autowired
    public ProfileController(UserRepository UserRepo)
    {
        this.UserRepo = UserRepo;
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

        User user = UserRepo.findByUsername(userName);
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
}
