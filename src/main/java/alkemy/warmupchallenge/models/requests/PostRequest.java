package alkemy.warmupchallenge.models.requests;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Builder
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostRequest {

    @NotBlank(message = "Title is required.")
    @Size(min = 3, max = 100, message = "The title must be between 3 and 100 characters ")
    private String title;

    @NotBlank(message = "The post body is required.")
    private String body;

    private String imageLink;

    @NotNull(message = "Post category is required.")
    private long category;

}
