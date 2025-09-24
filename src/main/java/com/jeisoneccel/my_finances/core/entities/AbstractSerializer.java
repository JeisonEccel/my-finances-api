package com.jeisoneccel.my_finances.core.entities;

import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public abstract class AbstractSerializer<E extends BasicEntity> extends StdSerializer<E> {

    public AbstractSerializer() {
        this(null);
    }

    public AbstractSerializer(Class<E> t) {
        super(t);
    }

    @Override
    public boolean isEmpty(SerializerProvider provider, E value) {
        return value == null;
    }

}

