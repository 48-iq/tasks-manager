package dev.ivanov.tasks_manager.auth_service.validators;

import dev.ivanov.tasks_manager.auth_service.entities.Role;
import dev.ivanov.tasks_manager.core.events.UserCredCreateEvent;
import dev.ivanov.tasks_manager.core.events.UserCredCreatedEvent;
import jakarta.annotation.Nonnull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

@Component
public class UserCredCreateEventValidator implements Validator {
    @Value("app.sign-up.admin.password")
    private String adminRegisterSecret;

    @Override
    public boolean supports(@Nonnull Class<?> clazz) {
        return UserCredCreatedEvent.class.equals(clazz);
    }

    @Override
    public void validate(@Nonnull Object target,@Nonnull Errors errors) {
        var userCredCreateEvent = (UserCredCreateEvent) target;
        var username = userCredCreateEvent.getUsername();
        var password = userCredCreateEvent.getPassword();
        var adminPassword = userCredCreateEvent.getAdminPassword();
        var role = userCredCreateEvent.getRole();

        if (username == null)
            errors.reject("username", "username is null");
        else {
            var pattern = Pattern.compile("^[a-zA-Z0-9_.]{3,128}$");
            var matcher = pattern.matcher(username);
            if (!matcher.matches())
                errors.reject("username", "username must contains only a-zA-Z9-9_. characters " +
                        "username length must be >= 3 and <= 128");
        }

        if (password == null)
            errors.reject("password", "password is null");
        else {
            var pattern = Pattern.compile("^.{8,128}$");
            var matcher = pattern.matcher(password);
            if (!matcher.matches())
                errors.reject("username", "username length must be >= 8 and <= 128");
        }

        if (role == null)
            errors.reject("role", "role is null");
        else {
            if (Arrays.stream(Role.values()).noneMatch(r -> r.name().equals(role)))
                errors.reject("role", "role must be in " + List.of(Role.values()));
            else if (Role.ROLE_ADMIN.name().equals(role)) {
                if (!adminRegisterSecret.equals(adminPassword))
                    errors.reject("adminPassword", "incorrect admin password");
            }
        }


    }
}
