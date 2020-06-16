package forum.web;

import forum.entity.Topic;
import forum.repository.CommentRepository;
import forum.repository.TopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comment")
public class CommentController {

    private CommentRepository comRepo;
    private TopicRepository topicRepo;

    @Autowired
    public CommentController(CommentRepository comRepo, TopicRepository topicRepo)
    {
        this.comRepo = comRepo;
        this.topicRepo = topicRepo;
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable("id") Long id)
    {
        Long topicId = comRepo.findById(id).get().getTopic().getId();
        comRepo.deleteById(id);

        if (comRepo.countByTopic_Id(topicId) == 0) topicRepo.deleteById(topicId);
    }

}
