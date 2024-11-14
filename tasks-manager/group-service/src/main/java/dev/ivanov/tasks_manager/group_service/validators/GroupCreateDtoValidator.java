package dev.ivanov.tasks_manager.group_service.validators;

import dev.ivanov.tasks_manager.group_service.dto.group.GroupCreateDto;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class GroupCreateDtoValidator implements Validator {
    @Override
    public boolean supports(@Nonnull Class<?> clazz) {
        return GroupCreateDto.class.equals(clazz);
    }

    @Override
    public void validate(@Nonnull Object target, @Nonnull Errors errors) {
        var groupCreateDto = (GroupCreateDto) target;
        var title = groupCreateDto.getTitle();
        var description = groupCreateDto.getDescription();
        if (!title.matches("^.{1,128}$"))
            errors.reject("title", "incorrect title");
        if (!description.matches("^.{1,512}$"))
            errors.reject("description", "incorrect description");
    }
}
