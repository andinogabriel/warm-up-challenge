package alkemy.warmupchallenge.models.requests;

import javax.validation.constraints.NotBlank;

public class CategoryRequest {

    @NotBlank(message = "Category name is required.")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
