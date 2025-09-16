package com.jeisoneccel.my_finances.classes.users;

import com.jeisoneccel.my_finances.core.repositories.BasicRepository;
import lombok.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends BasicRepository<User> {

    Optional<User> findByEmailIgnoreCase(@NonNull String email);

    List<User> findByIdNotAndEmail(@NonNull String id, @NonNull String email);

}
