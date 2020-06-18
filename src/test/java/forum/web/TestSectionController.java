package forum.web;

import forum.entity.Section;
import forum.entity.Topic;
import forum.forms.CreateTopicForm;
import forum.repository.CommentRepository;
import forum.repository.SectionRepository;
import forum.repository.TopicRepository;
import forum.repository.UserRepository;
import forum.security.UserRepositoryUserDetailsService;
import forum.service.Properties;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = SectionController.class)
public class TestSectionController {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepo;

    @MockBean
    private UserRepositoryUserDetailsService userRepoService;

    @MockBean
    private SectionRepository secRepo;

    @MockBean
    private TopicRepository topicRepo;

    @MockBean
    private CommentRepository comRepo;

    @MockBean
    private Properties props;

    @BeforeEach
    public void setUp()
    {
        Mockito.when(props.getTopicsCount()).thenReturn(10);
    }

    @Test
    void testSectionPageIfPresent() throws Exception
    {
        Section sec = new Section();
        sec.setName("test section");
        Mockito.when(secRepo.findById(anyLong())).thenReturn(Optional.ofNullable(sec));

        Topic topic = Mockito.mock(Topic.class);
        Page<Topic> topics = new PageImpl<>(Arrays.asList(topic, topic, topic));
        Mockito.when(topicRepo.findBySection_Id(anyLong(), any())).thenReturn(topics);
        Mockito.when(topic.getSum()).thenReturn(topic.new TopicSummary());

        RequestBuilder request = get("/section/{id}", 1);

        mockMvc.perform(request).andExpect(status().isOk())
                .andExpect(view().name("section"))
                .andExpect(content().string(Matchers.containsString("test section")))
                .andExpect(model().attributeExists("topics"))
                .andExpect(model().attribute("topics", Matchers.hasSize(3)))
                .andExpect(model().attributeExists("section"))
                .andExpect(model().attributeExists("userImg"))
                .andExpect(model().attributeExists("currentPage"))
                .andExpect(model().attributeExists("pageCount"))
                .andExpect(model().attributeExists("newTopic"));
    }

    @Test
    void testSectionPageIfNotPresent() throws Exception
    {
        RequestBuilder request = get("/section/{id}", 1);

        mockMvc.perform(request).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

    @Test
    void testTopicCreationIfNoErrors() throws Exception
    {
        Section sec = new Section();
        sec.setName("test section");
        Mockito.when(secRepo.findById(anyLong())).thenReturn(Optional.ofNullable(sec));

        Topic topic = new Topic();
        topic.setId((long)1);
        Mockito.when(topicRepo.save(any())).thenReturn(topic);

        CreateTopicForm form = new CreateTopicForm();
        form.setName("test");
        form.setText("test");

        RequestBuilder request = post("/section/{id}", 1)
                .flashAttr("newTopic", form)
                .with(csrf());

        mockMvc.perform(request).andExpect(status().is3xxRedirection())
                .andExpect(model().hasNoErrors())
                .andExpect(redirectedUrl("/topic/1?page=0"));
    }

    @Test
    void testTopicCreationIfErrors() throws Exception
    {
        Section sec = new Section();
        sec.setName("test section");
        Mockito.when(secRepo.findById(anyLong())).thenReturn(Optional.ofNullable(sec));

        CreateTopicForm form = new CreateTopicForm();
        form.setName(" ");
        form.setText(" ");

        Page<Topic> topics = new PageImpl<>(new ArrayList<>());
        Mockito.when(topicRepo.findBySection_Id(anyLong(), any())).thenReturn(topics);

        RequestBuilder request = post("/section/{id}", 1)
                .flashAttr("newTopic", form)
                .with(csrf());

        mockMvc.perform(request).andExpect(status().isOk())
                .andExpect(view().name("section"))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeExists("userImg"))
                .andExpect(model().attributeExists("section"))
                .andExpect(model().attributeExists("topics"));
    }

    @Test
    void testTopicCreationIfSectionIsNotPresent() throws Exception
    {
        RequestBuilder request = post("/section/{id}", 1)
                .flashAttr("newTopic", new CreateTopicForm())
                .with(csrf());

        mockMvc.perform(request).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

    @Test
    void testDeleteSection() throws Exception
    {
        mockMvc.perform(delete("/section/{id}", 1).with(csrf()))
            .andExpect(status().isNoContent());
    }
}
