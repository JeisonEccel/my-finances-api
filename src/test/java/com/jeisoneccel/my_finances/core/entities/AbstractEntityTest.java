package com.jeisoneccel.my_finances.core.entities;

import com.jeisoneccel.my_finances.utils.annotations.IgnoreOnUpdate;
import com.jeisoneccel.my_finances.utils.annotations.NotUpdatable;
import com.jeisoneccel.my_finances.utils.validations.ValidNotBlank;
import com.jeisoneccel.my_finances.utils.validations.ValidNotNull;
import com.jeisoneccel.my_finances.utils.validations.ValidString;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import org.hibernate.annotations.CreationTimestamp;
import org.junit.jupiter.api.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public abstract class AbstractEntityTest<E extends BasicEntity> {

    protected E givenEntity;
    protected List<Class<? extends Annotation>> stringRequiredAnnotations = List.of(ValidString.class);
    protected Set<Class<? extends Annotation>> notNullableRequiredAnnotations = Set.of(
            ValidNotNull.class, ValidNotBlank.class, CreationTimestamp.class
    );
    protected Set<Class<? extends Annotation>> notUpdatableRequiredAnnotations = Set.of(
            NotUpdatable.class, IgnoreOnUpdate.class, CreationTimestamp.class
    );

    @Test
    void givenEntity_AssertStringFieldsHaveAtLeastOneRequiredAnnotation() {
        Arrays.stream(givenEntity.getClass().getDeclaredFields())
                .filter(field -> field.getType().equals(String.class))
                .forEach(field -> assertThat(containsStringAnnotations(field)).isEqualTo(true));
    }

    @Test
    void givenFieldColumIsNotNullable_AssertRequiredAnnotationsArePresent() {
        givenEntity.getAllFields(givenEntity.getClass()).stream()
                .filter(this::containsNotNullableColumn)
                .forEach(field -> assertThat(containsNotNullableAnnotation(field)).isTrue());
    }

    @Test
    void givenFieldColumIsNotUpdatable_AssertRequiredAnnotationsArePresent() {
        givenEntity.getAllFields(givenEntity.getClass()).stream()
                .filter(this::containsNotUpdatableColumn)
                .forEach(field -> assertThat(containsNotUpdatableAnnotation(field)).isTrue());
    }

    private boolean containsStringAnnotations(Field field) {
        return stringRequiredAnnotations.stream().map(field::isAnnotationPresent).toList().contains(true);
    }

    private boolean containsNotNullableColumn(Field field) {
        Column column = field.getDeclaredAnnotation(Column.class);
        JoinColumn joinColumn = field.getDeclaredAnnotation(JoinColumn.class);

        if (column != null) {
            return !column.nullable();
        } else if (joinColumn != null) {
            return !joinColumn.nullable();
        }
        return false;
    }

    private boolean containsNotUpdatableColumn(Field field) {
        Column column = field.getDeclaredAnnotation(Column.class);
        JoinColumn joinColumn = field.getDeclaredAnnotation(JoinColumn.class);

        if (column != null) {
            return !column.updatable();
        } else if (joinColumn != null) {
            return !joinColumn.updatable();
        }
        return false;
    }

    private boolean containsNotNullableAnnotation(Field field) {
        for (Class<? extends Annotation> requiredAnnotation : notNullableRequiredAnnotations) {
            if (field.isAnnotationPresent(requiredAnnotation)) return true;
        }
        return false;
    }

    private boolean containsNotUpdatableAnnotation(Field field) {
        for (Class<? extends Annotation> requiredAnnotation : notUpdatableRequiredAnnotations) {
            if (field.isAnnotationPresent(requiredAnnotation)) return true;
        }
        return false;
    }

}

