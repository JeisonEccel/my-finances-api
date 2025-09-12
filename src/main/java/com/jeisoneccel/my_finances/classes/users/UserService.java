package com.jeisoneccel.my_finances.classes.users;

import com.jeisoneccel.my_finances.core.services.BasicService;
import com.jeisoneccel.my_finances.exceptions.custom.RecordAlreadyExistsException;
import com.jeisoneccel.my_finances.exceptions.custom.RecordNotFoundException;
import com.jeisoneccel.my_finances.utils.ServiceUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService, BasicService<User, UserModel> {

    private static final String TYPE = User.class.getSimpleName();

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final ServiceUtils serviceUtils;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info(TYPE + ": Fetching by username ({})", username);
        return repository.findByEmailIgnoreCase(username)
                .orElseThrow(() -> new UsernameNotFoundException(TYPE));
    }

    @Override
    public User getById(@NonNull String id) {
        log.info(TYPE + ": Fetching by id ({})", id);
        return repository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException(TYPE));
    }

    @Override
    public User create(@NonNull UserModel model) {
        log.info(TYPE + ": Creating new with model ({})", model);
        User entity = serviceUtils.mapModelToEntity(model, new User());
        validate(entity);
        entity.setPassword(encodePassword(model.getPassword()));
        return repository.save(entity);
    }

    @Override
    public User update(@NonNull String id, @NonNull HashMap<String, Object> updates) {
        log.info(TYPE + ": Updating id ({})", id);
        User entity = getById(id);
        if (updates.containsKey("name")) {
            entity.setName(updates.get("name").toString());
        }
        if (updates.containsKey("email")) {
            entity.setEmail(updates.get("email").toString());
        }
        if (updates.containsKey("password")) {
            entity.setPassword(updates.get("password").toString());
        }

        validate(entity);
        if (updates.containsKey("password")) {
            entity.setPassword(encodePassword(updates.get("password").toString()));
        }
        return repository.save(entity);
    }

    @Override
    public void delete(@NonNull String id) {
        log.info(TYPE + ": Deleting id ({})", id);
        repository.deleteById(id);
    }

    @Override
    public void validate(@NonNull User entity) {
        validateEmailIsAvailable(entity.getId(), entity.getEmail());
    }

    public void validateEmailIsAvailable(String id, String email) {
        log.info("Validating email is available: {}", email);
        String ignoreId = id != null ? id : "";
        if (!repository.findByIdNotAndEmail(ignoreId, email).isEmpty()) {
            throw new RecordAlreadyExistsException(TYPE, "email");
        }
    }

    public String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }
}
