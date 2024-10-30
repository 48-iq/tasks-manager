package dev.ivanov.tasks_manager.user_service.validators;

import dev.ivanov.tasks_manager.user_service.dto.UserUpdateDto;
import jakarta.annotation.Nonnull;
import org.apache.tomcat.util.file.Matcher;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.regex.Pattern;

@Component
public class UserUpdateDtoValidator implements Validator {
    @Override
    public boolean supports(@Nonnull Class<?> clazz) {
        return UserUpdateDto.class.equals(clazz);
    }

    @Override
    public void validate(@Nonnull Object target, @Nonnull Errors errors) {
        var userUpdateDto = (UserUpdateDto) target;
        var nickname = userUpdateDto.getNickname();
        var name = userUpdateDto.getName();
        var surname = userUpdateDto.getSurname();
        var email = userUpdateDto.getEmail();
        if (nickname == null)
            errors.reject("nickname", "nickname is null");
        else {
            var pattern = Pattern.compile("^.{0,128}$");
            var matcher = pattern.matcher(nickname);
            if (matcher.matches())
                errors.reject("nickname", "nickname too long");
        }
        if (name == null)
            errors.reject("name", "name is null");
        else {
            var pattern = Pattern.compile("^.{0,128}$");
            var matcher = pattern.matcher(name);
            if (matcher.matches())
                errors.reject("name", "name too long");
        }
        if (surname == null)
            errors.reject("surname", "surname is null");
        else {
            var pattern = Pattern.compile("^.{0,128}$");
            var matcher = pattern.matcher(surname);
            if (matcher.matches())
                errors.reject("surname", "surname too long");
        }
        if (email == null)
            errors.reject("email", "email is null");
        else {
            var pattern = Pattern.compile("^[a-zA-Z0-9\\-_.%]+@[a-zA-Z]+\\.[a-zA-Z]{2,6}$");
            var matcher = pattern.matcher(email);
            if (matcher.matches())
                errors.reject("email", "email is incorrect");
        }
    }
}
