package dev.aletheia.doctor.rules;

import dev.aletheia.doctor.annotations.Unique;
import dev.aletheia.doctor.config.ServiceUtils;
import dev.aletheia.doctor.services.ValidationService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class UniqueConstraintValidator implements ConstraintValidator<Unique, String> {

    private String column;
    private String table;
    private ValidationService validationService;


    @Override
    public void initialize(Unique constraintAnnotation) {
        this.column = constraintAnnotation.value();
        this.table = constraintAnnotation.table();

        this.validationService = ServiceUtils.getValidationService();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) return true;

        return validationService.isUnique(table, column, value);
    }
}

