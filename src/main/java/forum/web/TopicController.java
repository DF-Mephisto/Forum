package forum.web;

import forum.entity.Comment;
import forum.entity.Topic;
import forum.entity.User;
import forum.repository.CommentRepository;
import forum.repository.TopicRepository;
import forum.service.Properties;
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
import java.util.Optional;

@Controller
@RequestMapping("/topic")
public class TopicController {

    private TopicRepository topicRepo;
    private CommentRepository comRepo;
    private Properties props;

    @Autowired
    public TopicController(TopicRepository topicRepo,
                             CommentRepository comRepo,
                             Properties props)
    {
        this.topicRepo = topicRepo;
        this.comRepo = comRepo;
        this.props = props;
    }

    @ModelAttribute(name="userImg")
    public String userImg(@AuthenticationPrincipal User user)
    {
        return user == null ? "" : user.getImageStr();
    }

    @GetMapping("/{id}")
    public String getTopicPage(@PathVariable("id") Long id,
                               @RequestParam(value = "page", defaultValue = "0") int page,
                               Model model)
    {
        Pageable pageable = PageRequest.of(page, props.getCommentsCount(),
                Sort.by(Sort.Direction.ASC, "placedAt"));
        Optional<Topic> topicRes = topicRepo.findById(id);
        if (topicRes.isEmpty()) return "redirect:/";

        Topic topic = topicRes.get();
        topic.setViews(topic.getViews() + 1);
        topicRepo.save(topic);

        List<Comment> comments = comRepo.findByTopic_Id(id, pageable);

        model.addAttribute("topic", topic);
        model.addAttribute("comments", comments);
        model.addAttribute("newComment", new Comment());

        return "topic";
    }

    @PostMapping("/{id}")
    public String processPostComment(@PathVariable("id") Long id,
                                     @Valid @ModelAttribute("newComment") Comment comment,
                                     BindingResult errors,
                                     @RequestParam(value = "page", defaultValue = "0") int page,
                                     @AuthenticationPrincipal User user,
                                     Model model)
    {
        Optional<Topic> topicRes = topicRepo.findById(id);
        if (topicRes.isEmpty()) return "redirect:/";
        Topic topic = topicRes.get();

        if (errors.hasErrors())
        {
            Pageable pageable = PageRequest.of(page, props.getCommentsCount(),
                    Sort.by(Sort.Direction.ASC, "placedAt"));
            List<Comment> comments = comRepo.findByTopic_Id(id, pageable);
            model.addAttribute("topic", topic);
            model.addAttribute("comments", comments);
            return "topic";
        }

        comment.setTopic(topic);
        comment.setUser(user);
        comRepo.save(comment);

        String redirectUrl = "topic/" + id + "?page=" + page + "&vscroll=true";
        return "redirect:/" + redirectUrl;
    }
}
