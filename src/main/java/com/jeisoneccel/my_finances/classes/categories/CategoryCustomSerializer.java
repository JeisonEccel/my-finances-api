package com.jeisoneccel.my_finances.classes.categories;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.jeisoneccel.my_finances.core.entities.AbstractSerializer;

import java.io.IOException;

public class CategoryCustomSerializer extends AbstractSerializer<Category> {

    @Override
    public void serialize(
            Category category,
            JsonGenerator generator,
            SerializerProvider provider
    ) throws IOException {
        if (isEmpty(provider, category)) return;

        generator.writeStartObject();
        generator.writeStringField("id", category.getId());
        generator.writeStringField("name", category.getName());
        generator.writeEndObject();
    }

}
