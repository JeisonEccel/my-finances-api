package com.jeisoneccel.my_finances.classes.transactions;

import com.jeisoneccel.my_finances.auth.LoggedUser;
import com.jeisoneccel.my_finances.classes.accounts.Account;
import com.jeisoneccel.my_finances.classes.accounts.AccountService;
import com.jeisoneccel.my_finances.classes.categories.Category;
import com.jeisoneccel.my_finances.classes.categories.CategoryService;
import com.jeisoneccel.my_finances.classes.users.User;
import com.jeisoneccel.my_finances.core.entities.AbstractServiceTest;
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
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest extends AbstractServiceTest<TransactionService, TransactionRepository, Transaction, TransactionModel> {

    private final String givenDescription = "Test User";
    private final User givenUser = new User();
    @Mock
    TransactionRepository repository;
    @Mock
    LoggedUser loggedUser;
    @Mock
    AccountService accountService;
    @Mock
    CategoryService categoryService;

    @BeforeEach
    void setUp() {
        underTest = new TransactionService(repository, serviceUtils, loggedUser, accountService, categoryService);
        mockRepository = repository;
        entityClass = Transaction.class;

        givenId = "00000000-0000-0000-0000-000000000000";
        givenEntity = new Transaction();
        givenEntity.setId(givenId);
        givenEntity.setDescription(givenDescription);
        givenEntity.setAccount(new Account());
        givenEntity.setCategory(new Category());

        givenModel = new TransactionModel();
        givenModel.setDescription(givenDescription);

        givenUpdates = new HashMap<>();
        givenUpdates.put("description", givenDescription);
    }

    @Override
    public Transaction newEntity() {
        return new Transaction();
    }

    /*
    getAll
     */

    @Test
    void givenEntitiesNotExist_whenGetAll_AssertThatEmptyList() {
//        given
        List<Transaction> emptyList = new ArrayList<>();
        given(loggedUser.getUser()).willReturn(givenUser);
        given(repository.findByOwner(givenUser)).willReturn(emptyList);
//        when
        List<Transaction> result = underTest.getAll();
//        then
        assertThat(result).isEqualTo(emptyList);
    }

    @Test
    void givenEntitiesExist_whenGetAll_AssertThatListIsNotEmpty() {
//        given
        List<Transaction> entityList = new ArrayList<>();
        entityList.add(givenEntity);
        given(loggedUser.getUser()).willReturn(givenUser);
        given(repository.findByOwner(givenUser)).willReturn(entityList);
//        when
        List<Transaction> result = underTest.getAll();
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
        Transaction result = underTest.getById(givenId);
//        then
        assertThat(result).isEqualTo(givenEntity);
    }

    /*
    delete
     */

    @Override
    protected void givenValidId_WhenDelete_AssertEquals() {
//        given
        TransactionService mockedService = spy(underTest);
        doReturn(givenEntity).when(underTest).getById(givenId);
//        when
        mockedService.delete(givenId);
//        then
        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(mockRepository).deleteById(argumentCaptor.capture());
        String captured = argumentCaptor.getValue();
        assertThat(captured).isEqualTo(givenId);
    }

}
