package alkemy.warmupchallenge.models.requests;

import alkemy.warmupchallenge.utils.PasswordMatches;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@PasswordMatches
@Getter @Setter
public class UserRegisterRequest {

    @NotBlank(message = "Email is required.")
    @Email(message = "Invalid email.")
    private String email;

    @NotBlank(message = "Password is required.")
    @Size(min = 8, max = 30, message = "Password must have a range of 8 to 30 characters.")
    private String password;

    @NotBlank(message = "Confirmation password is required.")
    @Size(min = 8, max = 30, message = "Password must be a range of 8 to 30 characters.")
    private String matchingPassword;

}
