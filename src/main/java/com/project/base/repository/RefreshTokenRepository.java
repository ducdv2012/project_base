package com.project.base.repository;

import com.project.base.model.RefreshTokens;
import com.project.base.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshTokens, Long> {
    RefreshTokens findByToken(String refreshToken);

    @Modifying
    void deleteByUsers(Users user);
}
