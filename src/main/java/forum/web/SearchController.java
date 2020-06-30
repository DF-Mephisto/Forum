package forum.web;

import forum.entity.Topic;
import forum.entity.User;
import forum.repository.CommentRepository;
import forum.repository.TopicRepository;
import forum.service.Properties;
import forum.service.SummaryLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/search")
public class SearchController {

    private TopicRepository topicRepo;
    private Properties props;
    private SummaryLoader sumLoader;

    @Autowired
    public SearchController(TopicRepository topicRepo,
                            Properties props,
                            SummaryLoader sumLoader)
    {
        this.topicRepo = topicRepo;
        this.props = props;
        this.sumLoader = sumLoader;
    }

    @ModelAttribute(name="userImg")
    public String userImg(@AuthenticationPrincipal User user)
    {
        return user == null ? "" : user.getImageStr();
    }

    @GetMapping
    public String getTopicList(@RequestParam(value = "name", defaultValue = "") String name,
                               @RequestParam(value = "page", defaultValue = "0") int page,
                               Model model)
    {
        Pageable pageable = PageRequest.of(page, props.getTopicsCount());

        List<Topic> topics = topicRepo.findByNameIgnoreCaseContaining(name, pageable);
        sumLoader.fillTopicsSummary(topics);

        long pageCount = (long)Math.ceil((double) topicRepo.countByNameIgnoreCaseContaining(name) / (double)props.getTopicsCount());
        model.addAttribute("currentPage", page);
        model.addAttribute("pageCount", pageCount);
        model.addAttribute("name", name);
        model.addAttribute("topics", topics);

        return "search";
    }

}
