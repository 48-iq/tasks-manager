package dev.ivanov.tasks_manager.group_service.validators;

import dev.ivanov.tasks_manager.group_service.dto.topic.TopicCreateDto;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class TopicCreateDtoValidator implements Validator {
    @Override
    public boolean supports(@Nonnull Class<?> clazz) {
        return TopicCreateDto.class.equals(clazz);
    }

    @Override
    public void validate(@Nonnull Object target, @Nonnull Errors errors) {
        var topicCreateDto = (TopicCreateDto) target;
        var title = topicCreateDto.getTitle();
        var description = topicCreateDto.getDescription();
        var complexity = topicCreateDto.getComplexity();
        var importance = topicCreateDto.getImportance();
        var theme = topicCreateDto.getTheme();

        if (!title.matches("^.{1,128}$"))
            errors.reject("title", "incorrect title");
        if (!description.matches("^.{1,512}$"))
            errors.reject("description", "incorrect description");
        if (!(complexity >= 1 && complexity <= 10))
            errors.reject("complexity", "incorrect complexity");
        if (!(importance >= 1 && importance <= 10))
            errors.reject("importance", "incorrect importance");
        if (!theme.matches("^.{1,128}$"))
            errors.reject("incorrect theme");
    }
}
