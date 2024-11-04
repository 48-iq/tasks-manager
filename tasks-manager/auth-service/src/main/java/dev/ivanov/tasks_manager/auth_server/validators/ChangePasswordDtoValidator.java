package dev.ivanov.tasks_manager.auth_server.validators;

import dev.ivanov.tasks_manager.auth_server.dto.ChangePasswordDto;
import jakarta.annotation.Nonnull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class ChangePasswordDtoValidator implements Validator {

    @Autowired
    private RefreshJwtValidator refreshJwtValidator;

    @Override
    public boolean supports(@Nonnull Class<?> clazz) {
        return ChangePasswordDto.class.equals(clazz);
    }

    @Override
    public void validate(@Nonnull Object target, @Nonnull Errors errors) {
        var changePasswordDto = (ChangePasswordDto) target;
        var newPassword = changePasswordDto.getNewPassword();
        var refresh = changePasswordDto.getRefresh();

        refreshJwtValidator.validate(refresh, errors);

        if (newPassword == null)
            errors.reject("new password", "new password is null");
        else if (!newPassword.matches("^.{8,128}$"))
            errors.reject("new password", "new password is incorrect");
    }


}
