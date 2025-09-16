package com.jeisoneccel.my_finances.utils;

import com.jeisoneccel.my_finances.exceptions.ErrorCode;
import com.jeisoneccel.my_finances.exceptions.custom.CustomConstraintViolationException;
import com.jeisoneccel.my_finances.utils.date_time.DateTimeParse;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;
import java.util.Map;

import static com.jeisoneccel.my_finances.exceptions.ErrorCode.*;

@Component
public class DateTimeUtils {

    private final Map<Class<?>, DateTimeParse> dateTimeParseMethods = Map.of(
            ZonedDateTime.class, new DateTimeParse(ZonedDateTime::parse, ERR0V00001),
            LocalDate.class, new DateTimeParse(LocalDate::parse, ERR0V00002),
            LocalDateTime.class, new DateTimeParse(LocalDateTime::parse, ERR0V00003),
            LocalTime.class, new DateTimeParse(LocalTime::parse, ERR0V00004)
    );

    public DateTimeParse getDateTimeParse(Class<?> fieldType) {
        return dateTimeParseMethods.get(fieldType);
    }

    public boolean fieldTypeIsSupportedDateType(Field field) {
        return dateTimeParseMethods.containsKey(field.getType());
    }

    public Object convertStringToDate(Field field, Object value) {
        try {
            return getDateTimeParse(field.getType()).parseMethod().apply(value.toString());
        } catch (DateTimeParseException e) {
            ErrorCode errorCode = getDateTimeParse(field.getType()).parseErrorCode()
                    .withEntity(field.getDeclaringClass().getSimpleName())
                    .withFieldName(field.getName());
            throw new CustomConstraintViolationException(errorCode);
        }
    }

}