package com.jeisoneccel.my_finances.core.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.jeisoneccel.my_finances.utils.annotations.IgnoreOnUpdate;
import com.jeisoneccel.my_finances.utils.annotations.NotUpdatable;
import com.jeisoneccel.my_finances.utils.validations.*;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
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
    protected Set<Class<? extends Annotation>> basicEntityRequiredAnnotations = Set.of(
            ValidNestedEntity.class, OneToOne.class
    );
    protected Set<Class<? extends Annotation>> notNullableRequiredAnnotations = Set.of(
            ValidNotNull.class, ValidNotBlank.class, CreationTimestamp.class
    );
    protected Set<Class<? extends Annotation>> notUpdatableRequiredAnnotations = Set.of(
            NotUpdatable.class, IgnoreOnUpdate.class, CreationTimestamp.class
    );
    protected Set<Class<? extends Annotation>> serializerRequiredAnnotations = Set.of(
            JsonSerialize.class, JsonManagedReference.class, JsonBackReference.class
    );

    @Test
    void givenEntity_AssertStringFieldsHaveAtLeastOneRequiredAnnotation() {
        Arrays.stream(givenEntity.getClass().getDeclaredFields())
                .filter(field -> field.getType().equals(String.class))
                .forEach(field -> assertThat(containsStringAnnotations(field)).isEqualTo(true));
    }

    @Test
    void givenEntityWithNestedEntityFields_AssertRequiredAnnotationIsPresent() {
        boolean containsNestedField = givenEntity.getAllFields(givenEntity.getClass()).stream()
                .anyMatch(field -> BasicEntity.class.isAssignableFrom(field.getType()));

        if (containsNestedField) {
            assertThat(entityContainsAnnotation(givenEntity, ValidEntityWithNestedField.class)).isTrue();
        }
    }

    @Test
    void givenBasicEntityFields_AssertRequiredAnnotationsArePresent() {
        givenEntity.getAllFields(givenEntity.getClass()).stream()
                .filter(field -> BasicEntity.class.isAssignableFrom(field.getType()))
                .forEach(field -> assertThat(containsBasicEntityAnnotation(field)).isTrue());
    }

    @Test
    void givenFieldsAnnotatedWithValidNestedEntity_AssertTypeIsBasicEntity() {
        givenEntity.getAllFields(givenEntity.getClass()).stream()
                .filter(this::containsBasicEntityAnnotation)
                .forEach(field -> assertThat(BasicEntity.class.isAssignableFrom(field.getType())).isTrue());
    }

    @Test
    void givenBasicEntityOrSetField_AssertJsonSerializerAnnotationIsPresent() {
        givenEntity.getAllFields(givenEntity.getClass()).stream()
                .filter(this::isBasicEntityOrSet)
                .forEach(field -> assertThat(containsSerializerAnnotation(field)).isTrue());
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

    private boolean entityContainsAnnotation(E givenEntity, Class<? extends Annotation> annotation) {
        Class<?> clazz = givenEntity.getClass();
        while (clazz != Object.class) {
            if (clazz.isAnnotationPresent(annotation)) return true;

            clazz = clazz.getSuperclass();
        }
        return false;
    }

    private boolean containsBasicEntityAnnotation(Field field) {
        for (Class<? extends Annotation> requiredAnnotation : basicEntityRequiredAnnotations) {
            if (field.isAnnotationPresent(requiredAnnotation)) return true;
        }
        return false;
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

    private boolean containsSerializerAnnotation(Field field) {
        for (Class<? extends Annotation> requiredAnnotation : serializerRequiredAnnotations) {
            if (field.isAnnotationPresent(requiredAnnotation)) return true;
        }
        return false;
    }

    private boolean isBasicEntityOrSet(Field field) {
        return BasicEntity.class.isAssignableFrom(field.getType()) || Set.class.isAssignableFrom(field.getType());
    }

}

