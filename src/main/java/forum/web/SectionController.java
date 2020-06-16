package forum.web;

import forum.entity.Comment;
import forum.entity.Section;
import forum.entity.Topic;
import forum.entity.User;
import forum.forms.CreateTopicForm;
import forum.repository.CommentRepository;
import forum.repository.TopicRepository;
import forum.repository.UserRepository;
import forum.service.Properties;
import forum.repository.SectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/section")
public class SectionController {

    private SectionRepository secRepo;
    private TopicRepository topicRepo;
    private CommentRepository comRepo;
    private UserRepository userRepo;
    private Properties props;

    @Autowired
    public SectionController(SectionRepository secRepo,
                             TopicRepository topicRepo,
                             CommentRepository comRepo,
                             UserRepository userRepo,
                             Properties props)
    {
        this.secRepo = secRepo;
        this.topicRepo = topicRepo;
        this.comRepo = comRepo;
        this.userRepo = userRepo;
        this.props = props;
    }

    @ModelAttribute(name="userImg")
    public String userImg(@AuthenticationPrincipal User user)
    {
        return user == null ? "" : user.getImageStr();
    }


    @GetMapping("/{id}")
    public String getSectionPage(@PathVariable("id") Long id,
                                 @RequestParam(value = "page", defaultValue = "0") int page,
                                 Model model)
    {
        Pageable pageable = PageRequest.of(page, props.getTopicsCount());
        Optional<Section> secRes = secRepo.findById(id);
        if (secRes.isEmpty()) return "redirect:/";

        Section section = secRes.get();
        List<Topic> topics = topicRepo.findBySection_Id(id, pageable).getContent();
        fillTopicsSummary(topics);

        long pageCount = (long)Math.ceil((double) topicRepo.countBySection_Id(id) / (double)props.getTopicsCount());
        model.addAttribute("currentPage", page);
        model.addAttribute("pageCount", pageCount);

        model.addAttribute("section", section);
        model.addAttribute("topics", topics);
        model.addAttribute("newTopic", new CreateTopicForm());

        return "section";
    }

    @PostMapping("/{id}")
    public String processTopicCreation(@PathVariable("id") Long id,
                                       @Valid @ModelAttribute("newTopic") CreateTopicForm form,
                                       BindingResult errors,
                                       @RequestParam(value = "page", defaultValue = "0") int page,
                                       @AuthenticationPrincipal User user,
                                       Model model)
    {
        Optional<Section> secRes = secRepo.findById(id);
        if (secRes.isEmpty()) return "redirect:/";
        Section section = secRes.get();

        if (errors.hasErrors())
        {
            Pageable pageable = PageRequest.of(page, props.getTopicsCount());
            List<Topic> topics = topicRepo.findBySection_Id(id, pageable).getContent();
            fillTopicsSummary(topics);
            model.addAttribute("section", section);
            model.addAttribute("topics", topics);
            return "section";
        }

        Topic topic = new Topic();
        topic.setName(form.getName());
        topic.setSection(section);
        topic.setViews((long)0);
        topicRepo.save(topic);

        Comment comment = new Comment();
        comment.setText(form.getText());
        comment.setTopic(topic);
        comment.setUser(user);
        comRepo.save(comment);

        String redirectUrl = "topic/" + topic.getId() + "?page=" + page;
        return "redirect:/" + redirectUrl;
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deleteSection(@PathVariable("id") Long id)
    {
        secRepo.deleteById(id);
    }

    private void fillTopicsSummary(List<Topic> topics)
    {
        topics.forEach(t -> {
            Long topicId = t.getId();
            TopicRepository.lastPost lastPost = topicRepo.findLastPost(topicId);

            Topic.TopicSummary sum = t.getSum();
            if (lastPost != null)
            {
                sum.setLastPostUser(userRepo.findById(lastPost.getUserId()).orElseGet(null));
                sum.setLastPostComment(comRepo.findById(lastPost.getCommentId()).orElseGet(null));
            }

            sum.setTotalPosts(comRepo.countByTopic_Id(topicId));
        });
    }
}
