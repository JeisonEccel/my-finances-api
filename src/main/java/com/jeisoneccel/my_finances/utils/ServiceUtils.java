package com.jeisoneccel.my_finances.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jeisoneccel.my_finances.utils.annotations.IgnoreTrim;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Component
@RequiredArgsConstructor
public class ServiceUtils {

    private final ObjectMapper objectMapper;
    private final DateTimeUtils dateTimeUtils;
    private final StringUtils stringUtils;

    public <E, M> E mapModelToEntity(@NonNull M model, @NonNull E entity) {
        List<Field> modelFields = getAllFields(model.getClass());
        for (Field modelField : modelFields) {
            String fieldName = modelField.getName();
            modelField.setAccessible(true);

            Field entityField = ReflectionUtils.findField(entity.getClass(), fieldName);
            if (entityField != null) {
                entityField.setAccessible(true);
                Object modelValue = ReflectionUtils.getField(modelField, model);
                Object convertedValue = convertValueType(entityField, modelValue);
                Object preparedValue = trimValueIfRequired(entityField, convertedValue);
                ReflectionUtils.setField(entityField, entity, preparedValue);
            }
        }
        return entity;
    }

    public List<Field> getAllFields(Class<?> clazz) {
        if (clazz == Object.class) return new ArrayList<>();

        List<Field> declaredFields = Arrays.stream(clazz.getDeclaredFields())
                .filter(field -> !Modifier.isStatic(field.getModifiers()))
                .toList();
        List<Field> superClassFields = getAllFields(clazz.getSuperclass());
        return Stream.concat(declaredFields.stream(), superClassFields.stream()).collect(Collectors.toList());
    }

    private Object convertValueType(@NonNull Field field, Object value) {
        if (value == null) return null;

        if (value instanceof BigDecimal bigDecimalValue && field.getType() != BigDecimal.class) {
            return bigDecimalValue.stripTrailingZeros().toPlainString();
        }

        if (dateTimeUtils.fieldTypeIsSupportedDateType(field)) {
            return dateTimeUtils.convertStringToDate(field, value);
        }

        if (!field.getType().isAssignableFrom(value.getClass()) && !field.getType().isPrimitive()) {
            return objectMapper.convertValue(value, field.getType());
        }

        return value;
    }

    public Object trimValueIfRequired(@NonNull Field field, Object value) {
        Annotation ignoreTrim = field.getDeclaredAnnotation(IgnoreTrim.class);
        if (value != null && ignoreTrim == null && field.getType() == String.class) {
            String stringValue = String.valueOf(value);
            return stringUtils.trimAll(stringValue);
        }
        return value;
    }
}
