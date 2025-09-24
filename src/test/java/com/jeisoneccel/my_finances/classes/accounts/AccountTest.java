package com.jeisoneccel.my_finances.classes.accounts;

import com.jeisoneccel.my_finances.core.entities.AbstractEntityTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class AccountTest extends AbstractEntityTest<Account> {

    @BeforeEach
    void setUp() {
        givenEntity = new Account();
    }

}
