package com.jeisoneccel.my_finances.classes.transactions;

import com.jeisoneccel.my_finances.core.entities.AbstractEntityTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TransactionTest extends AbstractEntityTest<Transaction> {

    @BeforeEach
    void setUp() {
        givenEntity = new Transaction();
    }

}
