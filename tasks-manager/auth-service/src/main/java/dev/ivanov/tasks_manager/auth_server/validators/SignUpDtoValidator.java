package dev.ivanov.tasks_manager.auth_server.validators;

import dev.ivanov.tasks_manager.auth_server.dto.SignUpDto;
import dev.ivanov.tasks_manager.auth_server.repositories.postgres.AccountRepository;
import dev.ivanov.tasks_manager.auth_server.repositories.postgres.RoleRepository;
import dev.ivanov.tasks_manager.auth_server.repositories.redis.UsernameCacheRepository;
import jakarta.annotation.Nonnull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class SignUpDtoValidator implements Validator {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UsernameCacheRepository usernameCacheRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Value("${app.sign-up.admin.password}")
    private String validAdminPassword;

    @Override
    public boolean supports(@Nonnull Class<?> clazz) {
        return SignUpDto.class.equals(clazz);
    }

    @Override
    public void validate(@Nonnull Object target, @Nonnull Errors errors) {
        var signUpDto = (SignUpDto) target;
        var username = signUpDto.getUsername();
        var roles = signUpDto.getRoles();
        var password = signUpDto.getPassword();
        var adminPassword = signUpDto.getAdminPassword();


        if (username == null)
            errors.reject("username", "username is null");
        else if (!username.matches("^.{3,128}$"))
            errors.reject("username", "incorrect username");

        if (password == null)
            errors.reject("password", "password is null");
        else if (!password.matches("^.{8,128}$"))
            errors.reject("password", "incorrect password");

        var validRoles = roleRepository.findAll();

        for (var role: roles)
            if (validRoles.stream().noneMatch(r -> r.getName().equals(role)))
                errors.reject("role", "role " + role + " is not exists");

        if (roles.stream().anyMatch("ROLE_ADMIN"::equals))
            if (!validAdminPassword.equals(adminPassword))
                errors.reject("adminPassword", "incorrect admin password");

        if (accountRepository.existsByUsername(username))
            errors.reject("username", "username already exists");

        if (usernameCacheRepository.existsById(username))
            errors.reject("username", "username already exists");
    }
}
