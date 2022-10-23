package com.project.base.repository;

import com.project.base.model.RefreshToken;
import com.project.base.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    RefreshToken findByToken(String refreshToken);

    @Modifying
    void deleteByUsers(Users user);
}
