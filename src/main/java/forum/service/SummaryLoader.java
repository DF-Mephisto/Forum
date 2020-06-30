package forum.service;

import forum.entity.Section;
import forum.entity.Topic;
import forum.repository.CommentRepository;
import forum.repository.SectionRepository;
import forum.repository.TopicRepository;
import forum.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SummaryLoader {

    private SectionRepository secRepo;
    private TopicRepository topicRepo;
    private CommentRepository comRepo;
    private UserRepository userRepo;

    @Autowired
    public SummaryLoader(SectionRepository secRepo,
                             TopicRepository topicRepo,
                             CommentRepository comRepo,
                             UserRepository userRepo)
    {
        this.secRepo = secRepo;
        this.topicRepo = topicRepo;
        this.comRepo = comRepo;
        this.userRepo = userRepo;
    }

    public void fillTopicsSummary(List<Topic> topics)
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

    public void fillSectionSummary(List<Section> sections)
    {
        sections.forEach(s -> {
            Long id = s.getId();
            SectionRepository.lastPost lastPost = secRepo.findLastPost(id);

            Section.SectionSummary sum = s.getSum();
            if (lastPost != null)
            {
                sum.setLastPostTopicId(lastPost.getTopicid());
                sum.setLastPostTopicName(lastPost.getTopicName());
                sum.setLastPostUsername(lastPost.getUsername());
                sum.setLastPostUserrole(lastPost.getUserRole());
            }

            sum.setTotalTopics(secRepo.countTopics(id));
            sum.setTotalComments(secRepo.countComments(id));
        });
    }
}
