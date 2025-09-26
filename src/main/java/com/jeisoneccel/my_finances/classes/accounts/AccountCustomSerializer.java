package com.jeisoneccel.my_finances.classes.accounts;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.jeisoneccel.my_finances.core.entities.AbstractSerializer;

import java.io.IOException;

public class AccountCustomSerializer extends AbstractSerializer<Account> {

    @Override
    public void serialize(
            Account account,
            JsonGenerator generator,
            SerializerProvider provider
    ) throws IOException {
        if (isEmpty(provider, account)) return;

        generator.writeStartObject();
        generator.writeStringField("id", account.getId());
        generator.writeStringField("name", account.getName());
        generator.writeEndObject();
    }

}
