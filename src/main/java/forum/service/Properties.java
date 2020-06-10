package forum.service;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "forum")
public class Properties {
    private int topicsCount = 1;
    private int commentsCount = 1;

    public void setTopicsCount(int topicsCount)
    {
        this.topicsCount = topicsCount;
    }
    public void setCommentsCount(int commentsCount)
    {
        this.commentsCount = commentsCount;
    }
}
