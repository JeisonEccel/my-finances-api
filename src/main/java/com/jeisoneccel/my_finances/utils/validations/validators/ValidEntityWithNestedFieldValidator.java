package com.jeisoneccel.my_finances.utils.validations.validators;

import com.jeisoneccel.my_finances.core.entities.BasicEntity;
import com.jeisoneccel.my_finances.core.repositories.BasicRepository;
import com.jeisoneccel.my_finances.utils.ServiceUtils;
import com.jeisoneccel.my_finances.utils.validations.ValidEntityWithNestedField;
import com.jeisoneccel.my_finances.utils.validations.ValidNestedEntity;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Optional;

import static com.jeisoneccel.my_finances.MyFinancesApplication.appContext;
import static com.jeisoneccel.my_finances.exceptions.ErrorCode.ERR0S00001;

@Slf4j
public class ValidEntityWithNestedFieldValidator
        implements ConstraintValidator<ValidEntityWithNestedField, BasicEntity> {

    private final ServiceUtils serviceUtils;

    public ValidEntityWithNestedFieldValidator() {
        this.serviceUtils = appContext.getBean(ServiceUtils.class);
    }

    @Override
    public boolean isValid(BasicEntity entity, ConstraintValidatorContext cxt) {
        for (Field field : entity.getAllFields(entity.getClass())) {
            ValidNestedEntity fieldAnnotation = field.getAnnotation(ValidNestedEntity.class);
            if (fieldAnnotation == null) continue;

            field.setAccessible(true);
            Object fieldValue = ReflectionUtils.getField(field, entity);
            if (fieldValue == null) continue;

            if (!(fieldValue instanceof BasicEntity nestedEntity)) {
                buildConstraintViolationMessage(cxt, field, ERR0S00001.name());
                return false;
            }

            if (nestedEntity.getId() == null) {
                if (fieldAnnotation.acceptsNullId()) continue;

                buildConstraintViolationMessage(cxt, field, fieldAnnotation.message());
                return false;
            }

            if (fieldAnnotation.skipRepositoryLoad()) continue;

            BasicRepository<?> repository = serviceUtils.getRepositoryBeanForEntity(field.getType());
            Optional<?> nestedEntityFromDB = repository.findById(nestedEntity.getId());
            if (nestedEntityFromDB.isEmpty()) {
                buildConstraintViolationMessage(cxt, field, fieldAnnotation.message());
                return false;
            }

            ReflectionUtils.setField(field, entity, nestedEntityFromDB.get());
        }

        return true;
    }

    private void buildConstraintViolationMessage(
            ConstraintValidatorContext cxt, Field field, String errorCode
    ) {
        cxt.disableDefaultConstraintViolation();
        cxt.buildConstraintViolationWithTemplate(errorCode)
                .addPropertyNode(field.getName())
                .addConstraintViolation();
    }

}
