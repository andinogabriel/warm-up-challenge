package alkemy.warmupchallenge.models.requests;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter @Setter
public class PostRequest {

    @NotBlank(message = "Title is required.")
    @Size(min = 3, max = 100, message = "The title must be between 3 and 100 characters ")
    private String title;

    @NotBlank(message = "The post body is required.")
    private String body;

    private String image;

    @NotNull(message = "Post category is required.")
    private long category;

    @NotNull(message = "Post creator is required.")
    private long user;

}
