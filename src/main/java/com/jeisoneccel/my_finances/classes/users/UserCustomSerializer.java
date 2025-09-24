package com.jeisoneccel.my_finances.classes.users;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.jeisoneccel.my_finances.core.entities.AbstractSerializer;

import java.io.IOException;

public class UserCustomSerializer extends AbstractSerializer<User> {

    @Override
    public void serialize(
            User user,
            JsonGenerator generator,
            SerializerProvider provider
    ) throws IOException {
        if (isEmpty(provider, user)) return;

        generator.writeStartObject();
        generator.writeStringField("id", user.getId());
        generator.writeStringField("name", user.getName());
        generator.writeStringField("email", user.getEmail());
        generator.writeEndObject();
    }

}
