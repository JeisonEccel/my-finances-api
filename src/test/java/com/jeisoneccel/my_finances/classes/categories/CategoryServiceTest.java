package com.jeisoneccel.my_finances.classes.categories;

import com.jeisoneccel.my_finances.auth.LoggedUser;
import com.jeisoneccel.my_finances.classes.users.User;
import com.jeisoneccel.my_finances.core.entities.AbstractServiceTest;
import com.jeisoneccel.my_finances.exceptions.custom.RecordAlreadyExistsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest extends AbstractServiceTest<CategoryService, CategoryRepository, Category, CategoryModel> {

    private final String givenName = "Test User";
    private final User givenUser = new User();
    @Mock
    CategoryRepository repository;
    @Mock
    LoggedUser loggedUser;

    @BeforeEach
    void setUp() {
        underTest = new CategoryService(repository, serviceUtils, loggedUser);
        mockRepository = repository;
        entityClass = Category.class;

        givenId = "00000000-0000-0000-0000-000000000000";
        givenEntity = new Category();
        givenEntity.setId(givenId);
        givenEntity.setName(givenName);

        givenModel = new CategoryModel();
        givenModel.setName(givenName);

        givenUpdates = new HashMap<>();
        givenUpdates.put("name", givenName);
    }

    @Override
    public Category newEntity() {
        return new Category();
    }

    /*
    getAll
     */

    @Test
    void givenEntitiesNotExist_whenGetAll_AssertThatEmptyList() {
//        given
        List<Category> emptyList = new ArrayList<>();
        given(loggedUser.getUser()).willReturn(givenUser);
        given(repository.findByOwner(givenUser)).willReturn(emptyList);
//        when
        List<Category> result = underTest.getAll();
//        then
        assertThat(result).isEqualTo(emptyList);
    }

    @Test
    void givenEntitiesExist_whenGetAll_AssertThatListIsNotEmpty() {
//        given
        List<Category> entityList = new ArrayList<>();
        entityList.add(givenEntity);
        given(loggedUser.getUser()).willReturn(givenUser);
        given(repository.findByOwner(givenUser)).willReturn(entityList);
//        when
        List<Category> result = underTest.getAll();
//        then
        assertThat(result).isEqualTo(entityList);
    }

    /*
    getById
     */

    @Override
    protected void givenValidId_WhenGetById_AssertEquals() {
//        given
        given(loggedUser.getUser()).willReturn(givenUser);
        given(repository.findByIdAndOwner(givenId, givenUser)).willReturn(Optional.of(givenEntity));
//        when
        Category result = underTest.getById(givenId);
//        then
        assertThat(result).isEqualTo(givenEntity);
    }

    /*
    delete
     */

    @Override
    protected void givenValidId_WhenDelete_AssertEquals() {
//        given
        CategoryService mockedService = spy(underTest);
        doReturn(givenEntity).when(underTest).getById(givenId);
//        when
        mockedService.delete(givenId);
//        then
        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(mockRepository).deleteById(argumentCaptor.capture());
        String captured = argumentCaptor.getValue();
        assertThat(captured).isEqualTo(givenId);
    }

    /*
    validateNameIsAvailable()
     */

    @Test
    void givenNameExists_WhenValidateNameIsAvailable_WillThrowException() {
//        given
        List<Category> givenList = new ArrayList<>();
        givenList.add(givenEntity);
        given(repository.findByIdNotAndNameIgnoreCaseAndOwner(givenId, givenName, givenUser))
                .willReturn(givenList);
//        when
//        then
        assertThatThrownBy(() -> underTest.validateNameIsAvailable(givenId, givenName, givenUser))
                .isInstanceOf(RecordAlreadyExistsException.class);
    }

}
