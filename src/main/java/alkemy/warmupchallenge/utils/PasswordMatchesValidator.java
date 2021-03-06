package alkemy.warmupchallenge.utils;

import alkemy.warmupchallenge.models.requests.UserRegisterRequest;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, Object> {

    @Override
    public void initialize(PasswordMatches constraintAnnotation) {
    }

    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext context) {
        UserRegisterRequest user = (UserRegisterRequest) obj;
        return user.getPassword().equals(user.getMatchingPassword());
    }
}