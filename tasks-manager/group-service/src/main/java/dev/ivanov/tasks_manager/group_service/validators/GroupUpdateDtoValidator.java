package dev.ivanov.tasks_manager.group_service.validators;

import dev.ivanov.tasks_manager.group_service.dto.group.GroupUpdateDto;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class GroupUpdateDtoValidator implements Validator {
    @Override
    public boolean supports(@Nonnull Class<?> clazz) {
        return GroupUpdateDto.class.equals(clazz);
    }

    @Override
    public void validate(@Nonnull Object target, @Nonnull Errors errors) {
        var groupUpdateDto = (GroupUpdateDto) target;
        var title = groupUpdateDto.getTitle();
        var description = groupUpdateDto.getDescription();
        if (!title.matches("^.{1,128}$"))
            errors.reject("title", "incorrect title");
        if (!description.matches("^.{1,512}$"))
            errors.reject("description", "incorrect description");
    }
}
