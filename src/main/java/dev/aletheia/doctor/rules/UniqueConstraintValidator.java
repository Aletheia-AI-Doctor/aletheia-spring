package dev.aletheia.doctor.rules;

import dev.aletheia.doctor.annotations.Unique;
import dev.aletheia.doctor.config.ServiceUtils;
import dev.aletheia.doctor.services.ValidationService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UniqueConstraintValidator implements ConstraintValidator<Unique, String> {

    private String column;
    private String table;

    private ValidationService validationService;

    @Override
    public void initialize(Unique constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        this.column = constraintAnnotation.value();
        this.table = constraintAnnotation.table();

        validationService = ServiceUtils.getValidationService();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        return validationService.isUnique(table, column, value);
    }
}

