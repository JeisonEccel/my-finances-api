package com.jeisoneccel.my_finances.classes.users;

import com.jeisoneccel.my_finances.core.entities.AbstractEntityTest;
import com.jeisoneccel.my_finances.utils.validations.ValidPassword;
import com.jeisoneccel.my_finances.utils.validations.ValidString;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

@ExtendWith(MockitoExtension.class)
public class UserTest extends AbstractEntityTest<User> {

    @BeforeEach
    void setUp() {
        givenEntity = new User();
        stringRequiredAnnotations = List.of(ValidString.class, ValidPassword.class);
    }

}
