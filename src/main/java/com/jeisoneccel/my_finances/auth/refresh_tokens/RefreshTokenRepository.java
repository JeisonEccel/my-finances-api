package com.jeisoneccel.my_finances.auth.refresh_tokens;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {

}
