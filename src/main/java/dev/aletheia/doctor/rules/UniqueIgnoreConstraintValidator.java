package dev.aletheia.doctor.rules;

import dev.aletheia.doctor.annotations.UniqueIgnore;
import dev.aletheia.doctor.config.ServiceUtils;
import dev.aletheia.doctor.services.ValidationService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.lang.reflect.Field;

public class UniqueIgnoreConstraintValidator implements ConstraintValidator<UniqueIgnore, Object> {

    private String table;
    private String[] fields;
    private String[] columns;
    private String idField;
    private String idColumn;
    private ValidationService validationService;

    @Override
    public void initialize(UniqueIgnore constraintAnnotation) {
        this.table = constraintAnnotation.table();
        this.fields = constraintAnnotation.fields();
        this.columns = constraintAnnotation.columns();
        this.idField = constraintAnnotation.idField();
        this.idColumn = constraintAnnotation.idColumn();
        this.validationService = ServiceUtils.getValidationService();
    }

    @Override
    public boolean isValid(Object object, ConstraintValidatorContext context) {
        if (object == null) return true;

        try {
            Object idValue = getFieldValue(object, idField);
            Object[] values = new Object[fields.length];

            for (int i = 0; i < fields.length; i++) {
                values[i] = getFieldValue(object, fields[i]);
            }


            boolean isValid = true;
            for (int i = 0; i < fields.length; i++) {
                String column = columns[i];
                Object value = values[i];

                if (value != null) {
                    boolean exists = ! validationService.isUnique(table, column, value.toString(), idColumn, idValue.toString());
                    if (exists) {
                        addFieldError(context, fields[i], value.toString(), column);
                        isValid = false;
                    }
                }
            }

            return isValid;
        } catch (Exception e) {
            handleException(context, e);
            return false;
        }
    }

    private Object getFieldValue(Object object, String fieldName)
            throws NoSuchFieldException, IllegalAccessException {
        Field field = object.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(object);
    }

    private void addFieldError(ConstraintValidatorContext context, String fieldName,
                               String value, String column) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(String.format("%s already exists.", fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1)))
                .addPropertyNode(fieldName)
                .addConstraintViolation();
    }

    private void handleException(ConstraintValidatorContext context, Exception e) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(e.getMessage()).addConstraintViolation();
    }
}