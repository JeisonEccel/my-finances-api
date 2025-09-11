package com.jeisoneccel.my_finances.core.services;

import lombok.NonNull;

import java.util.HashMap;

public interface BasicService<E, M> {

    E getById(@NonNull String id);

    E create(@NonNull M model);

    E update(@NonNull String id, @NonNull HashMap<String, Object> updates);

    void delete(@NonNull String id);

    void validate(@NonNull E entity);

}
