package forum.web;

import forum.entity.Section;
import forum.entity.User;
import forum.repository.SectionRepository;
import forum.service.Properties;
import forum.service.SummaryLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/")
public class HomeController {

    private SectionRepository secRepo;
    private Properties props;
    private SummaryLoader sumLoader;

    @Autowired
    public HomeController(SectionRepository secRepo,
                          Properties props,
                          SummaryLoader sumLoader)
    {
        this.secRepo = secRepo;
        this.props = props;
        this.sumLoader = sumLoader;
    }

    @ModelAttribute
    public void addAttributes(@RequestParam(value = "page", defaultValue = "0") int page,
                              @AuthenticationPrincipal User user,
                              Model model)
    {
        Pageable pageable = PageRequest.of(page, props.getTopicsCount(),
                Sort.by(Sort.Direction.ASC, "placedAt"));
        List<Section> sections = secRepo.findAll(pageable);
        sumLoader.fillSectionSummary(sections);

        model.addAttribute("sections", sections);

        String userImg = user == null ? "" : user.getImageStr();
        model.addAttribute("userImg", userImg);

        long pageCount = (long)Math.ceil((double)secRepo.count() / (double)props.getTopicsCount());
        model.addAttribute("currentPage", page);
        model.addAttribute("pageCount", pageCount);
    }

    @GetMapping
    public String getHomePage(Model model)
    {
        model.addAttribute("newSection", new Section());

        return "index";
    }

    @PostMapping
    public String createSection(@Valid @ModelAttribute("newSection") Section section,
                                BindingResult errors)
    {
        if (errors.hasErrors())
        {
            return "index";
        }

        secRepo.save(section);

        return "redirect:/";
    }
}
