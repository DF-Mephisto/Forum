package forum.web;

import forum.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comment")
public class CommentController {

    private CommentRepository comRepo;

    @Autowired
    public CommentController(CommentRepository comRepo)
    {
        this.comRepo = comRepo;
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable("id") Long id)
    {
        comRepo.deleteById(id);
    }

}
