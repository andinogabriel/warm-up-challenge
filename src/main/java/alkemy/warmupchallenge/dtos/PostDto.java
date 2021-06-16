package alkemy.warmupchallenge.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

@Getter @Setter
public class PostDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private long id;

    @NotBlank(message = "Title is required.")
    @Size(min = 3, max = 100, message = "The title must be between 3 and 100 characters ")
    private String title;

    @NotBlank(message = "The post body is required.")
    private String body;

    private String image;

    @JsonIgnoreProperties(value = {"posts", "handler","hibernateLazyInitializer"}, allowSetters = true)
    @NotNull
    private CategoryDto category;

    private Date creationDate;
    private UserDto user;

}
