package dev.aletheia.doctor.annotations;

import dev.aletheia.doctor.rules.UniqueIgnoreConstraintValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = UniqueIgnoreConstraintValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueIgnore {
    String table();
    String[] fields();
    String[] columns();
    String idField() default "id";
    String idColumn() default "id";
    String message() default "is not unique";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
