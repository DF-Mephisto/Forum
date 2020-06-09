package forum.web;

import forum.entity.Section;
import forum.entity.User;
import forum.repository.SectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/")
@ConfigurationProperties(prefix = "forum")
public class HomeController {

    private SectionRepository secRepo;

    private int pageSize = 1;

    public void setPageSize(int pageSize)
    {
        this.pageSize = pageSize;
    }

    @Autowired
    public HomeController(SectionRepository secRepo)
    {
        this.secRepo = secRepo;
    }

    @ModelAttribute(name="userImg")
    public String userImg(@AuthenticationPrincipal User user)
    {
        return user == null ? "" : user.getImageStr();
    }

    @GetMapping
    public String getHomePage(@RequestParam(value = "page", defaultValue = "0") int page,
                       Model model)
    {
        Pageable pageable = PageRequest.of(page, pageSize);
        List<Section> sections = secRepo.findAll(pageable);
        model.addAttribute("sections", sections);

        return "index";
    }

}
