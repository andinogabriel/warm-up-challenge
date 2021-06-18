package alkemy.warmupchallenge.models.responses;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter @Setter
public class PostResponse {

    private long id;
    private String title;
    private String body;
    private String imageLink;
    private boolean deleted;
    private CategoryResponse category;
    private Date creationDate;
    private UserResponse user;

}
