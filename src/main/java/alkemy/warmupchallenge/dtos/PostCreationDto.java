package alkemy.warmupchallenge.dtos;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter @Setter
public class PostCreationDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private long id;
    private String title;
    private String body;
    private String image;
    private long category;
    private Date creationDate;
    private String user;

}
