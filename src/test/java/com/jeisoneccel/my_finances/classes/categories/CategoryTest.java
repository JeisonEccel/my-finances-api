package com.jeisoneccel.my_finances.classes.categories;

import com.jeisoneccel.my_finances.core.entities.AbstractEntityTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CategoryTest extends AbstractEntityTest<Category> {

    @BeforeEach
    void setUp() {
        givenEntity = new Category();
    }

}
