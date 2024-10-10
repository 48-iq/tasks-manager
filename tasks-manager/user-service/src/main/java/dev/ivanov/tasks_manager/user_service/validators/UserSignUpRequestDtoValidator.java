package dev.ivanov.tasks_manager.user_service.validators;

import dev.ivanov.tasks_manager.user_service.dto.UserSignUpDto;
import dev.ivanov.tasks_manager.user_service.repositories.UserRepository;
import jakarta.annotation.Nonnull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.regex.Pattern;

@Component
public class UserSignUpRequestDtoValidator implements Validator {
    @Autowired
    private UserRepository userRepository;

    @Override
    public boolean supports(@Nonnull Class<?> clazz) {
        return UserSignUpDto.class.equals(clazz);
    }

    @Override
    public void validate(@Nonnull Object target, @Nonnull Errors errors) {
        var userSignUpRequestDto = (UserSignUpDto) target;
        var username = userSignUpRequestDto.getUsername();
        var name = userSignUpRequestDto.getName();
        var surname = userSignUpRequestDto.getSurname();
        var email = userSignUpRequestDto.getEmail();

        if (username == null)
            errors.reject("username", "username is null");
        else {
            var pattern = Pattern.compile("^[a-zA-Z0-9_.]{3,128}$");
            var matcher = pattern.matcher(username);
            if (!matcher.matches())
                errors.reject("username", "username must contains only a-zA-Z0-9_. characters and " +
                        "username length must be >=3 and <= 128");
            else if (userRepository.existsByUsername(username))
                errors.reject("username", "username already exists");
        }

        var namePattern = Pattern.compile("^.{1,128}");
        if (name != null) {
            var matcher = namePattern.matcher(name);
            if (!matcher.matches())
                errors.reject("name", "name length must be >= 1 <= 128");
        }
        if (surname != null) {
            var matcher = namePattern.matcher(surname);
            if (!matcher.matches())
                errors.reject("surname", "surname length must be >= 1 <= 128");
        }
        if (email != null) {
            var pattern = Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$");
            var matcher = pattern.matcher(email);
            if (!matcher.matches())
                errors.reject("email", "invalid email");
        }
    }
}
