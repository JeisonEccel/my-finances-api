package com.jeisoneccel.my_finances.core.entities;

import com.jeisoneccel.my_finances.core.repositories.BasicRepository;
import com.jeisoneccel.my_finances.core.services.BasicService;
import com.jeisoneccel.my_finances.exceptions.custom.RecordNotFoundException;
import com.jeisoneccel.my_finances.utils.ServiceUtils;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;

import java.util.HashMap;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@RequiredArgsConstructor
public abstract class AbstractServiceTest
        <S extends BasicService<E, M>, R extends BasicRepository<E>, E extends BasicEntity, M> {

    @Mock
    protected ServiceUtils serviceUtils;
    protected R mockRepository;
    protected S underTest;
    protected String givenId;
    protected E givenEntity;
    protected M givenModel;
    protected Class<E> entityClass;

    protected HashMap<String, Object> givenUpdates;

    /*
    getById()
     */

    @Test
    void givenValidId_WhenGetById_AssertEquals() {
//        given
        given(mockRepository.findById(givenId))
                .willReturn(Optional.of(givenEntity));
        E expected = givenEntity;
//        when
        E result = underTest.getById(givenId);
//        then
        assertThat(result).isEqualTo(expected);
    }

    @Test
    void givenIdNotExists_WhenGetById_WillThrowException() {
//        given
//        when
//        then
        assertThatThrownBy(() -> underTest.getById(givenId))
                .isInstanceOf(RecordNotFoundException.class);
    }

    @Test
    void givenIdIsNull_WhenGetById_WillThrowException() {
//        given
        String nullId = null;
//        when
//        then
        assertThatThrownBy(() -> underTest.getById(nullId))
                .isInstanceOf(IllegalArgumentException.class);
    }

    /*
    create()
     */

    @Test
    void givenValidModel_WhenCreate_AssertEquals() {
//        given
        given(serviceUtils.mapModelToEntity(givenModel, newEntity())).willReturn(givenEntity);
        S mockedService = spy(underTest);
        doNothing().when(mockedService).validate(givenEntity);
//        when
        mockedService.create(givenModel);
//        then
        ArgumentCaptor<E> argumentCaptor = ArgumentCaptor.forClass(entityClass);
        verify(mockRepository).save(argumentCaptor.capture());
        E captured = argumentCaptor.getValue();
        captured.setId(givenId);
        assertThat(captured).isEqualTo(givenEntity);
    }

    @Test
    void givenModelHasInvalidProperty_WhenCreate_WillThrowException() {
//        given
        given(serviceUtils.mapModelToEntity(givenModel, newEntity())).willReturn(givenEntity);
        S mockedService = spy(underTest);
        doThrow(ConstraintViolationException.class).when(mockedService).validate(givenEntity);
//        when
//        then
        assertThatThrownBy(() -> mockedService.create(givenModel))
                .isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    void givenModelIsNull_WhenCreate_WillThrowException() {
//        given
        M nullModel = null;
//        when
//        then
        assertThatThrownBy(() -> underTest.create(nullModel))
                .isInstanceOf(IllegalArgumentException.class);
    }

    /*
    update()
     */

    @Test
    void givenValidHashMap_WhenUpdate_AssertEquals() {
//        given
        S mockedService = spy(underTest);
        doReturn(givenEntity).when(mockedService).getById(givenId);
        given(serviceUtils.mapHashToEntity(givenUpdates, givenEntity)).willReturn(givenEntity);
        doNothing().when(mockedService).validate(givenEntity);
//        when
        mockedService.update(givenId, givenUpdates);
//        then
        ArgumentCaptor<E> argumentCaptor = ArgumentCaptor.forClass(entityClass);
        verify(mockRepository).save(argumentCaptor.capture());
        E captured = argumentCaptor.getValue();
        assertThat(captured).isEqualTo(givenEntity);
    }

    @Test
    void givenIdIsNotValid_WhenUpdate_WillThrowException() {
//        given
//        when
//        then
        assertThatThrownBy(() -> underTest.update(givenId, givenUpdates))
                .isInstanceOf(RecordNotFoundException.class);
    }

    @Test
    void givenIdIsNull_WhenUpdate_WillThrowException() {
//        given
        String nullId = null;
//        when
//        then
        assertThatThrownBy(() -> underTest.update(nullId, givenUpdates))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void givenHashMapHasInvalidProperty_WhenUpdate_WillThrowException() {
//        given
        S mockedService = spy(underTest);
        doReturn(givenEntity).when(mockedService).getById(givenId);
        given(serviceUtils.mapHashToEntity(givenUpdates, givenEntity)).willReturn(givenEntity);
        doThrow(ConstraintViolationException.class).when(mockedService).validate(givenEntity);
//        when
//        then
        assertThatThrownBy(() -> mockedService.update(givenId, givenUpdates))
                .isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    void givenHashMapIsNull_WhenUpdate_WillThrowException() {
//        given
        HashMap<String, Object> nullHashMap = null;
//        when
//        then
        assertThatThrownBy(() -> underTest.update(givenId, nullHashMap))
                .isInstanceOf(IllegalArgumentException.class);
    }

    /*
    delete()
     */

    @Test
    void givenValidId_WhenDelete_AssertEquals() {
//        given
        S mockedService = spy(underTest);
//        when
        mockedService.delete(givenId);
//        then
        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(mockRepository).deleteById(argumentCaptor.capture());
        String captured = argumentCaptor.getValue();
        assertThat(captured).isEqualTo(givenId);
    }

    @Test
    void givenIdNotExists_WhenDelete_WillThrowException() {
//        given
//        when
//        then
        assertDoesNotThrow(() -> underTest.delete(givenId));
    }

    @Test
    void givenIdIsNull_WhenDelete_WillThrowException() {
//        given
        String nullId = null;
//        when
//        then
        assertThatThrownBy(() -> underTest.delete(nullId))
                .isInstanceOf(IllegalArgumentException.class);
    }

    public abstract E newEntity();

}

