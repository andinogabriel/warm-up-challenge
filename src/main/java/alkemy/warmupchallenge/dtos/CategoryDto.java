package alkemy.warmupchallenge.dtos;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter @Setter
public class CategoryDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private long id;
    private String name;
    private List<PostDto> posts;

}
