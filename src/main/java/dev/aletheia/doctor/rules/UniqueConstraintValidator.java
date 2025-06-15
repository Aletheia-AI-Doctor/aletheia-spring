package dev.aletheia.doctor.rules;

import dev.aletheia.doctor.annotations.Unique;
import dev.aletheia.doctor.config.ServiceUtils;
import dev.aletheia.doctor.services.ValidationService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

public class UniqueConstraintValidator implements ConstraintValidator<Unique, String> {

    private String column;
    private String table;

    @Override
    public void initialize(Unique constraintAnnotation) {
        this.column = constraintAnnotation.value();
        this.table = constraintAnnotation.table();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        if (value == null) return true;

        ValidationService validationService = ServiceUtils.getValidationService();
        System.out.println("Validating uniqueness for " + table + "." + column + " with value: " + value);
        return validationService.isUnique(table, column, value);
    }
}

