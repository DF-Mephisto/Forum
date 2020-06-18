package forum.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import forum.entity.Section;
import forum.repository.SectionRepository;
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
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = HomeController.class)
public class TestHomeController {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepo;

    @MockBean
    private UserRepositoryUserDetailsService userRepoService;

    @MockBean
    private SectionRepository secRepo;

    @MockBean
    private Properties props;

    private List<Section> sections;

    @BeforeEach
    public void setUp()
    {
        Section sec = Mockito.mock(Section.class);
        sections = Arrays.asList(sec, sec, sec);

        Mockito.when(props.getTopicsCount()).thenReturn(10);
        Mockito.when(secRepo.findAll(any())).thenReturn(sections);
        Mockito.when(sec.getSum()).thenReturn(sec.new SectionSummary());
    }

    @Test
    public void testHomePage() throws Exception
    {
        RequestBuilder request = get("/")
                .with(csrf());

        mockMvc.perform(request).andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(content().string(Matchers.containsString("Forum")))
                .andExpect(model().attributeExists("sections"))
                .andExpect(model().attribute("sections", Matchers.equalTo(sections)))
                .andExpect(model().attributeExists("userImg"))
                .andExpect(model().attributeExists("currentPage"))
                .andExpect(model().attributeExists("pageCount"))
                .andExpect(model().attributeExists("newSection"));
    }

    @Test
    public void testAddSectionWithErrors() throws Exception
    {
        Section sec = new Section();
        sec.setName(" ");

        RequestBuilder request = post("/")
                .flashAttr("newSection", sec)
                .with(csrf());

        mockMvc.perform(request).andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attributeExists("sections"))
                .andExpect(model().attribute("sections", Matchers.equalTo(sections)))
                .andExpect(model().attributeExists("newSection"))
                .andExpect(model().hasErrors());
    }

    @Test
    public void testAddSectionWithoutErrors() throws Exception
    {
        Section sec = new Section();
        sec.setName("Ok name");

        RequestBuilder request = post("/")
                .flashAttr("newSection", sec)
                .with(csrf());

        mockMvc.perform(request).andExpect(status().is3xxRedirection())
                .andExpect(model().hasNoErrors())
                .andExpect(redirectedUrl("/"));
    }
}
