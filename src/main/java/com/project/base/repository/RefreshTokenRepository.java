package com.project.base.repository;

import com.project.base.model.RefreshTokens;
import com.project.base.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshTokens, Long> {
    RefreshTokens findByToken(String refreshToken);

    @Modifying
    @Transactional
    @Query("DELETE FROM RefreshTokens r WHERE r.users = :user")
    void deleteByUsers(Users user);
}
