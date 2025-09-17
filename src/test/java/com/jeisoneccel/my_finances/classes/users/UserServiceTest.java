package com.jeisoneccel.my_finances.classes.users;

import com.jeisoneccel.my_finances.core.entities.AbstractServiceTest;
import com.jeisoneccel.my_finances.exceptions.custom.RecordAlreadyExistsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class UserServiceTest extends AbstractServiceTest<UserService, UserRepository, User, UserModel> {

    private final String givenEmail = "test@email.com";
    private final String givenName = "Test User";
    private final String givenPassword = "Password";
    @Mock
    UserRepository repository;
    @Mock
    PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        underTest = new UserService(repository, passwordEncoder, serviceUtils);
        mockRepository = repository;
        entityClass = User.class;

        givenId = "00000000-0000-0000-0000-000000000000";
        givenEntity = new User();
        givenEntity.setId(givenId);
        givenEntity.setEmail(givenEmail);

        givenModel = new UserModel();
        givenModel.setName(givenName);
        givenModel.setEmail(givenEmail);
        givenModel.setPassword(givenPassword);

        givenUpdates = new HashMap<>();
        givenUpdates.put("email", givenEmail);
    }

    @Override
    public User newEntity() {
        return new User();
    }

    /*
    loadUserByUsername()
     */

    @Test
    void givenValidUsername_whenLoadUserByUsername_thenAssertEquals() {
//        given
        given(repository.findByEmailIgnoreCase(givenEmail)).willReturn(Optional.of(givenEntity));
//        when
        User result = underTest.loadUserByUsername(givenEmail);
//        then
        assertThat(result).isEqualTo(givenEntity);
    }

    @Test
    void givenInvalidUsername_whenLoadUserByUsername_willThrowException() {
//        given
//        when
//        then
        assertThatThrownBy(() -> underTest.loadUserByUsername(givenEmail))
                .isInstanceOf(BadCredentialsException.class);
    }

    @Test
    void givenUsernameIsNull_whenLoadUserByUsername_willThrowException() {
//        given
        String nullUsername = null;
//        when
//        then
        assertThatThrownBy(() -> underTest.loadUserByUsername(nullUsername))
                .isInstanceOf(BadCredentialsException.class);
    }

    @Test
    void givenUsernameInUpperCase_whenLoadUserByUsername_thenAssertEquals() {
//        given
        String upperCaseUsername = givenEmail.toUpperCase();
        given(repository.findByEmailIgnoreCase(upperCaseUsername)).willReturn(Optional.of(givenEntity));
//        when
        User result = underTest.loadUserByUsername(upperCaseUsername);
//        then
        assertThat(result).isEqualTo(givenEntity);
    }

    /*
    validateEmailIsAvailable()
     */

    @Test
    void givenEmailExists_WhenValidateEmailIsAvailable_WillThrowException() {
//        given
        String givenEmail = "test@email.com";
        List<User> givenList = new ArrayList<>();
        givenList.add(givenEntity);
        given(repository.findByIdNotAndEmail(givenId, givenEmail))
                .willReturn(givenList);
//        when
//        then
        assertThatThrownBy(() -> underTest.validateEmailIsAvailable(givenId, givenEmail))
                .isInstanceOf(RecordAlreadyExistsException.class);
    }

}
